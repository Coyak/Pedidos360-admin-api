package com.pedidos360.admin.service;

import com.pedidos360.admin.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Servicio de administración de RabbitMQ para Pedidos360.
 * Encapsula la interacción directa con AmqpAdmin / RabbitAdmin, aislando a los controladores
 * de la API REST de detalles de bajo nivel del protocolo AMQP.
 */
@Service
public class RabbitAdminService {

    private static final Logger log = LoggerFactory.getLogger(RabbitAdminService.class);

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    /**
     * Declara y crea una nueva cola en RabbitMQ con soporte para configuración DLX opcional.
     */
    public OperacionResponseDTO crearCola(QueueCreateRequestDTO request) {
        String nombreCola = request.getNombre().trim();
        log.info("Creando cola '{}' (durable={}, exclusive={}, autoDelete={})",
                nombreCola, request.getDurable(), request.getExclusive(), request.getAutoDelete());

        Map<String, Object> args = new HashMap<>();
        if (request.getDeadLetterExchange() != null && !request.getDeadLetterExchange().trim().isEmpty()) {
            args.put("x-dead-letter-exchange", request.getDeadLetterExchange().trim());
            log.info("Configurando DLX '{}' en cola '{}'", request.getDeadLetterExchange(), nombreCola);
        }
        if (request.getDeadLetterRoutingKey() != null && !request.getDeadLetterRoutingKey().trim().isEmpty()) {
            args.put("x-dead-letter-routing-key", request.getDeadLetterRoutingKey().trim());
            log.info("Configurando routing key DLQ '{}' en cola '{}'", request.getDeadLetterRoutingKey(), nombreCola);
        }

        Queue queue = new Queue(nombreCola, request.getDurable(), request.getExclusive(), request.getAutoDelete(), args);
        amqpAdmin.declareQueue(queue);

        return new OperacionResponseDTO(
                "SUCCESS",
                "Cola '" + nombreCola + "' creada o verificada exitosamente en RabbitMQ",
                nombreCola,
                "CREATE_QUEUE"
        );
    }

    /**
     * Elimina una cola existente en RabbitMQ.
     */
    public OperacionResponseDTO eliminarCola(String nombreCola) {
        String nombreNormalizado = nombreCola.trim();
        log.info("Eliminando cola '{}' de RabbitMQ", nombreNormalizado);

        boolean eliminada = amqpAdmin.deleteQueue(nombreNormalizado);

        return new OperacionResponseDTO(
                eliminada ? "SUCCESS" : "NOT_FOUND_OR_SKIPPED",
                "Operación de eliminación ejecutada para la cola '" + nombreNormalizado + "'",
                nombreNormalizado,
                "DELETE_QUEUE"
        );
    }

    /**
     * Consulta información y propiedades estadísticas de una cola.
     */
    public QueueInfoResponseDTO obtenerInfoCola(String nombreCola) {
        String nombreNormalizado = nombreCola.trim();
        Properties props = amqpAdmin.getQueueProperties(nombreNormalizado);

        if (props == null) {
            throw new IllegalArgumentException("La cola '" + nombreNormalizado + "' no existe en el broker RabbitMQ");
        }

        Integer totalMensajes = (Integer) props.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
        Integer totalConsumidores = (Integer) props.get(RabbitAdmin.QUEUE_CONSUMER_COUNT);

        Map<String, Object> propiedadesMap = new HashMap<>();
        props.forEach((k, v) -> propiedadesMap.put(k.toString(), v));

        return new QueueInfoResponseDTO(nombreNormalizado, totalMensajes, totalConsumidores, propiedadesMap);
    }

    /**
     * Purga todos los mensajes contenidos en una cola.
     */
    public OperacionResponseDTO purgarCola(String nombreCola) {
        String nombreNormalizado = nombreCola.trim();
        log.info("Purgando mensajes en cola '{}'", nombreNormalizado);
        amqpAdmin.purgeQueue(nombreNormalizado, false);

        return new OperacionResponseDTO(
                "SUCCESS",
                "Cola '" + nombreNormalizado + "' purgada exitosamente",
                nombreNormalizado,
                "PURGE_QUEUE"
        );
    }

    /**
     * Declara y crea un nuevo Exchange según el tipo indicado (direct, topic, fanout, headers).
     */
    public OperacionResponseDTO crearExchange(ExchangeCreateRequestDTO request) {
        String nombreExchange = request.getNombre().trim();
        String tipo = request.getTipo().trim().toLowerCase();
        log.info("Creando exchange '{}' de tipo '{}'", nombreExchange, tipo);

        Exchange exchange;
        switch (tipo) {
            case "topic":
                exchange = new TopicExchange(nombreExchange, request.getDurable(), request.getAutoDelete());
                break;
            case "fanout":
                exchange = new FanoutExchange(nombreExchange, request.getDurable(), request.getAutoDelete());
                break;
            case "headers":
                exchange = new HeadersExchange(nombreExchange, request.getDurable(), request.getAutoDelete());
                break;
            case "direct":
            default:
                exchange = new DirectExchange(nombreExchange, request.getDurable(), request.getAutoDelete());
                break;
        }

        amqpAdmin.declareExchange(exchange);

        return new OperacionResponseDTO(
                "SUCCESS",
                "Exchange '" + nombreExchange + "' (" + tipo + ") creado exitosamente",
                nombreExchange,
                "CREATE_EXCHANGE"
        );
    }

    /**
     * Elimina un Exchange existente.
     */
    public OperacionResponseDTO eliminarExchange(String nombreExchange) {
        String nombreNormalizado = nombreExchange.trim();
        log.info("Eliminando exchange '{}'", nombreNormalizado);
        boolean eliminado = amqpAdmin.deleteExchange(nombreNormalizado);

        return new OperacionResponseDTO(
                eliminado ? "SUCCESS" : "NOT_FOUND_OR_SKIPPED",
                "Operación de eliminación para exchange '" + nombreNormalizado + "' procesada",
                nombreNormalizado,
                "DELETE_EXCHANGE"
        );
    }

    /**
     * Vincula (Binding) una cola a un exchange con una routing key específica.
     */
    public OperacionResponseDTO crearBinding(BindingCreateRequestDTO request) {
        String cola = request.getCola().trim();
        String exchange = request.getExchange().trim();
        String routingKey = request.getRoutingKey().trim();

        log.info("Creando binding: cola='{}' -> exchange='{}' con routingKey='{}'", cola, exchange, routingKey);

        Binding binding = new Binding(
                cola,
                Binding.DestinationType.QUEUE,
                exchange,
                routingKey,
                null
        );

        amqpAdmin.declareBinding(binding);

        return new OperacionResponseDTO(
                "SUCCESS",
                "Binding establecido: '" + cola + "' -> '" + exchange + "' ['" + routingKey + "']",
                cola + " <-> " + exchange,
                "CREATE_BINDING"
        );
    }

    /**
     * Desvincula (Unbind) una cola de un exchange.
     */
    public OperacionResponseDTO eliminarBinding(BindingCreateRequestDTO request) {
        String cola = request.getCola().trim();
        String exchange = request.getExchange().trim();
        String routingKey = request.getRoutingKey().trim();

        log.info("Eliminando binding: cola='{}' -> exchange='{}' con routingKey='{}'", cola, exchange, routingKey);

        Binding binding = new Binding(
                cola,
                Binding.DestinationType.QUEUE,
                exchange,
                routingKey,
                null
        );

        amqpAdmin.removeBinding(binding);

        return new OperacionResponseDTO(
                "SUCCESS",
                "Binding eliminado: '" + cola + "' de '" + exchange + "'",
                cola + " <-> " + exchange,
                "DELETE_BINDING"
        );
    }
}
