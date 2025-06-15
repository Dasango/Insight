package com.interfaces;

import com.modelo.Notificacion;

public interface IObserver {
    void notificacionRecibida(Notificacion notificacion);
}