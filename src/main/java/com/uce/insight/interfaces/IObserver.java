package com.uce.insight.interfaces;


import com.uce.insight.modelo.Notificacion;

public interface IObserver {
    void notificacionRecibida(Notificacion notificacion);
}