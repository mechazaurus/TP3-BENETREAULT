package com.ybene.projects.tp3_benetreault.services;

import android.os.Binder;

public class BackgroundServiceBinder extends Binder {

    private IBackgroundTimerService service = null;

    public BackgroundServiceBinder(IBackgroundTimerService service) {
        super();
        this.service = service;
    }

    public IBackgroundTimerService getService() {
        return service;
    }
}
