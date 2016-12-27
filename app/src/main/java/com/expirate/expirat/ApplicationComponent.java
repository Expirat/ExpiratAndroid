package com.expirate.expirat;


import dagger.Component;

@Component(
        modules = { ApplicationModule.class }
)
public interface ApplicationComponent extends ApplicationGraph {
}
