package com.ordonteam.inject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.api.Api
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
        injectActivityResult()
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


    Collection<Method> activityResults
    Collection<Method> activityResultsFailed

    void injectActivityResult() {
        this.activityResults = this.class.declaredMethods.findAll(has(InjectActivityResult))
        this.activityResultsFailed = this.class.declaredMethods.findAll(has(InjectActivityResultFailed))
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        Method proper = findMatchingResult(requestCode, responseCode)
        Method failed = findMatchingResultFailed(requestCode)

        if( proper != null && failed != null ) {
            (proper ?: failed).invoke(this, requestCode, responseCode, intent)
        }else{
            throw new IllegalArgumentException("There is no registred method for handling this pair of request/response codes. RequestCode = $requestCode ResponseCode = $responseCode")
        }
    }

    private Method findMatchingResult(int requestCode, int responseCode) {
        return activityResults.find { Method method ->
            InjectActivityResult activityResult = method.getAnnotation(InjectActivityResult)
            activityResult.requestCode() == requestCode &&
                    activityResult.responseCode() == responseCode
        }
    }

    private Method findMatchingResultFailed(int requestCode) {
        return activityResultsFailed.find { Method method ->
            method.getAnnotation(InjectActivityResultFailed).requestCode() == requestCode
        }
    }

}
