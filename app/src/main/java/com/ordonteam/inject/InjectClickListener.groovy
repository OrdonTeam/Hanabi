package com.ordonteam.inject

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
public @interface InjectClickListener {
    int value()
}