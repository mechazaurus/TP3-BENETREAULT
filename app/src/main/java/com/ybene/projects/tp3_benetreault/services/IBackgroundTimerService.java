package com.ybene.projects.tp3_benetreault.services;

public interface IBackgroundTimerService {

    public void addListener(IBackgroundTimerServiceListener listener);
    public void removeListener(IBackgroundTimerServiceListener listener);
}
