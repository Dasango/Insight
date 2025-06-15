package com.controladores;

import com.interfaces.IObserver;
import com.modelo.Notificacion;

public class NotificacionController implements IObserver {

    @Override
    public void notificacionRecibida(Notificacion notificacion) {
        // actualizar la vista, mostrar alertas, etc.
        System.out.println("Nueva notificación para usuario " + notificacion.getUsuarioId() + ": " + notificacion.getMensaje());
    }
}
