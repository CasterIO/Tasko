package com.donnfelker.tasko;

import com.donnfelker.tasko.fragments.MainFragment;
import com.donnfelker.tasko.services.CurrentConditionService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class, ApiModule.class })
public interface ApplicationComponent {
    void inject(TaskoApplication target);
    void inject(MainActivity target);
    void inject(CurrentConditionService target);
    void inject(MainFragment target);
}
