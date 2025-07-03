package com.uce.insight.modelo;

import com.uce.insight.interfaces.IObserver;
import com.uce.insight.services.NotificacionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaDeNotificaciones {

    private final NotificacionService notificacionService;
    private final List<IObserver> observadores = new ArrayList<>();

    public SistemaDeNotificaciones() {
        this.notificacionService = new NotificacionService();
    }

    // Método para registrar observadores
    public void registrarObservador(IObserver observador) {
        observadores.add(observador);
    }

    // Método para quitar observadores
    public void quitarObservador(IObserver  observador) {
        observadores.remove(observador);
    }

    // Método para notificar a los observadores
    private void notificarObservadores(Notificacion notificacion) {
        for (IObserver observador : observadores) {
            observador.notificacionRecibida(notificacion);
        }
    }

    // Enviar notificación a un usuario
    public void enviarNotificacion(String mensaje, Usuario usuario) {
        Notificacion notificacion = new Notificacion(
                generarId(),  // Este ID idealmente debería venir de la DB
                usuario.getId(),
                mensaje,
                false,
                LocalDateTime.now()
        );

        boolean resultado = notificacionService.crearNotificacion(notificacion);

        if (resultado) {
            System.out.println("Notificación enviada a " + usuario.getNombre() + ": " + mensaje);
            notificarObservadores(notificacion);  // Notifica a los observadores
        } else {
            System.out.println("Error al enviar la notificación.");
        }
    }

    public void obtenerNotificacionesDeUsuario(int usuarioId) {
        var notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuarioId);
        for (Notificacion notificacion : notificaciones) {
            System.out.println("Notificación para usuario " + usuarioId + ": " + notificacion.getMensaje());
        }
    }

    public void marcarNotificacionComoLeida(int notificacionId) {
        boolean resultado = notificacionService.marcarNotificacionComoLeida(notificacionId);
        if (resultado) {
            System.out.println("Notificación marcada como leída.");
        } else {
            System.out.println("Error al marcar la notificación como leída.");
        }
    }

    private int generarId() {
        return (int) (Math.random() * 100000);
    }
}
