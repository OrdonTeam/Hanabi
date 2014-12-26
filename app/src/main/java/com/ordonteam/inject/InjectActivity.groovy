package com.ordonteam.inject

import android.app.Activity
import android.os.Bundle
import android.view.View
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method

@CompileStatic
class InjectActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle)
        applyContentView()
        applyListeners()
        injectFields()
    }

    private void applyContentView() {
        InjectContentView layoutName = this.class.getAnnotation(InjectContentView)
        if (!layoutName)
            throw new RuntimeException('InjectActivity have to used together with InjectContentView')
        setContentView(layoutName.value())
    }

    @CompileDynamic
    private void applyListeners() {
        Collection<Method> methods = this.class.declaredMethods.findAll(has(InjectClickListener))
        methods.each { Method method ->
            InjectClickListener listener = method.getAnnotation(InjectClickListener)
            View foundView = findViewById(listener.value())
            foundView.setOnClickListener { View view ->
                method.invoke(this, view)
            }
        }
    }

    @CompileDynamic
    void injectFields() {
        Collection<Field> fields = this.class.declaredFields.findAll(has(InjectView))
        fields.each { Field field ->
            InjectView view = field.getAnnotation(InjectView)
            (this."${field.name}" = findViewById(view.value()))
        }
    }


    Closure<Closure<Boolean>> has = { Class aClass ->
        return { AnnotatedElement element ->
            element.getAnnotation(aClass) ? true : false
        }
    }

}
