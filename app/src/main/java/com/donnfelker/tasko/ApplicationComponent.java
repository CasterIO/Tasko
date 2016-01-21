package com.donnfelker.tasko;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {
    void inject(TaskoApplication target);
    void inject(MainActivity target);
}
