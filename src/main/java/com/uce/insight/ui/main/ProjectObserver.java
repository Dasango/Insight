package com.uce.insight.ui.main;

public class ProjectObserver {
    public static boolean hayCambios = false;

    public static void hayCambiosEnProyectos() {
        hayCambios = true;
    }

    public static void setHayCambios(boolean estado) {
        hayCambios = estado;
    }

    public static void noHayCambios() {
        hayCambios = false;
    }
}