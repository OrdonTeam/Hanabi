package com.ordonteam.inject

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@interface InjectContentView {
    int value()
}
