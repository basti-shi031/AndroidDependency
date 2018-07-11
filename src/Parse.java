import config.Config;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.SourceUnit;
import util.L;

public class Parse {
    public static void main(String args[]) {
        String source ="ext {\n" +
                "    // Android\n" +
                "    androidBuildToolsVersion = '25.0.2'\n" +
                "    androidMinSdkVersion = 16\n" +
                "    androidTargetSdkVersion = 25\n" +
                "    androidCompileSdkVersion = 25\n" +
                "    androidSupportSdkVersion = '25.2.0'\n" +
                "    releaseVersionCode = 1\n" +
                "    releaseVersionName = '1.0.0'\n" +
                "\n" +
                "    // Libraries\n" +
                "    multiDexVersion = '1.0.1'\n" +
                "\n" +
                "    daggerVersion = '2.9'\n" +
                "    butterKnifeVersion = '8.5.1'\n" +
                "\n" +
                "    okHttpVersion = '3.6.0'\n" +
                "    retrofitVersion = '2.2.0'\n" +
                "\n" +
                "    sqlDelightVersion = '0.5.1'\n" +
                "    autoBundleVersion = '4.1.0'\n" +
                "    onActivityResultVersion = '0.6.0'\n" +
                "    mvpVersion = '1.3.1'\n" +
                "    icePickVersion = '3.2.0'\n" +
                "    routerVersion = \"0.5.0\"\n" +
                "    compilerVersion = \"0.2.0\"\n" +
                "\n" +
                "    // Developer\n" +
                "    stethoVersion = '1.4.2'\n" +
                "\n" +
                "    // Testing\n" +
                "    jacocoVersion = '0.7.7.201606060606'\n" +
                "    dexmakerVersion = '1.4'\n" +
                "    jsonAssertVersion = '1.3.0'\n" +
                "\n" +
                "    espressoVersion = '2.2.2'\n" +
                "    testSupportLibVersion = '0.5'\n" +
                "\n" +
                "    baseDependencies =\n" +
                "            [androidSupportAppCompatV7: \"com.android.support:appcompat-v7:$androidSupportSdkVersion\",\n" +
                "             recyclerViewV7           : \"com.android.support:recyclerview-v7:$androidSupportSdkVersion\",\n" +
                "             safelyAndroid            : \"com.github.piasy:safelyandroid:1.2.4\",\n" +
                "             router                   : \"com.chenenyu.router:router:0.6.0\",\n" +
                "             multiDex                 : \"com.android.support:multidex:$multiDexVersion\",\n" +
                "\n" +
                "             javaxAnnotation          : \"org.glassfish:javax.annotation:10.0-b28\",\n" +
                "\n" +
                "             yaMvpDagger2             : \"com.github.piasy:YaMvp-Dagger2:$mvpVersion\",\n" +
                "             yaMvpRx                  : \"com.github.piasy:YaMvp-Rx:$mvpVersion\",\n" +
                "             rxJava                   : \"io.reactivex:rxjava:1.2.6\",\n" +
                "             rxAndroid                : \"io.reactivex:rxandroid:1.2.1\",\n" +
                "             rxJava2                  : \"io.reactivex.rxjava2:rxjava:2.0.6\",\n" +
                "             rxAndroid2               : \"io.reactivex.rxjava2:rxandroid:2.0.1\",\n" +
                "             rxJava2Interop           : \"com.github.akarnokd:rxjava2-interop:0.9.1\",\n" +
                "             rxBindingAppCompatV7     : \"com.jakewharton.rxbinding:rxbinding-appcompat-v7:1.0.0\",\n" +
                "             eventBus                 : \"org.greenrobot:eventbus:3.0.0\",\n" +
                "             dagger                   : \"com.google.dagger:dagger:$daggerVersion\",\n" +
                "             butterKnife              : \"com.jakewharton:butterknife:$butterKnifeVersion\",\n" +
                "\n" +
                "             gson                     : \"com.google.code.gson:gson:2.8.0\",\n" +
                "             okHttp                   : \"com.squareup.okhttp3:okhttp:$okHttpVersion\",\n" +
                "             retrofit                 : \"com.squareup.retrofit2:retrofit:$retrofitVersion\",\n" +
                "             retrofitGsonConverter    : \"com.squareup.retrofit2:converter-gson:$retrofitVersion\",\n" +
                "             retrofitRxJava2Adapter   : \"com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion\",\n" +
                "\n" +
                "             sqlBrite                 : 'com.squareup.sqlbrite:sqlbrite:1.1.1',\n" +
                "             sqlDelightRuntime        : \"com.squareup.sqldelight:runtime:$sqlDelightVersion\",\n" +
                "             rxPreferences            : \"com.f2prateek.rx.preferences2:rx-preferences:2.0.0-RC1\",\n" +
                "\n" +
                "             threetenabp              : \"com.jakewharton.threetenabp:threetenabp:1.0.5\",\n" +
                "             autoBundle               : \"com.github.yatatsu:autobundle:$autoBundleVersion\",\n" +
                "             icepick                  : \"frankiesardo:icepick:$icePickVersion\",\n" +
                "             onActivityResult         : \"com.vanniktech:onactivityresult:$onActivityResultVersion\",\n" +
                "\n" +
                "             mugen                    : \"com.vinaysshenoy:mugen:1.0.3\",\n" +
                "\n" +
                "             timber                   : \"com.jakewharton.timber:timber:4.5.1\",\n" +
                "             okHttpLogging            : \"com.squareup.okhttp3:logging-interceptor:$okHttpVersion\",\n" +
                "             ok2Curl                  : 'com.github.mrmike:ok2curl:0.3.1',\n" +
                "             stethoOkhttp             : \"com.facebook.stetho:stetho-okhttp3:$stethoVersion\",]\n" +
                "\n" +
                "    featureDependencies = [cardViewV7         : \"com.android.support:cardview-v7:$androidSupportSdkVersion\",\n" +
                "\n" +
                "                           rxJavaProGuardRules: \"com.artemzin.rxjava:proguard-rules:1.2.6.0\",\n" +
                "\n" +
                "                           fresco             : \"com.facebook.fresco:fresco:1.1.0\",\n" +
                "                           iconifyMD          : \"com.joanzapata.iconify:android-iconify-material:2.2.2\",\n" +
                "                           flexLayout         : \"com.github.mmin18:flexlayout:1.2.1\",\n" +
                "                           slidr              : \"com.r0adkll:slidableactivity:2.0.5\",\n" +
                "                           recyclerViewPager  : \"com.github.lsjwzh.RecyclerViewPager:lib:v1.1.0-beta5\",\n" +
                "                           statusBarUtil      : \"com.jaeger.statusbaruitl:library:1.3.5\",\n" +
                "\n" +
                "                           jsoup              : 'org.jsoup:jsoup:1.10.2',\n" +
                "\n" +
                "                           crashlytics        : 'com.crashlytics.sdk.android:crashlytics:2.6.6@aar',\n" +
                "                           answers            : 'com.crashlytics.sdk.android:answers:1.3.11@aar',\n" +
                "                           gitSha             : \"com.github.promeg:android-git-sha-lib:1.0.1\",\n" +
                "\n" +
                "                           stetho             : \"com.facebook.stetho:stetho:$stethoVersion\",\n" +
                "                           devMetrics         : \"com.frogermcs.androiddevmetrics:androiddevmetrics-runtime-noop:0.4\",\n" +
                "                           strictmodeNotifier : \"com.nshmura:strictmode-notifier:0.9.3\",\n" +
                "                           blockCanary        : \"com.github.markzhai:blockcanary-android:1.4.1\",\n" +
                "                           anrWatchDog        : \"com.github.anrwatchdog:anrwatchdog:1.3.0\",\n" +
                "                           leakCanary         : \"com.squareup.leakcanary:leakcanary-android:1.5\",]\n" +
                "\n" +
                "    aptDependencies = [daggerApt          : \"com.google.dagger:dagger-compiler:$daggerVersion\",\n" +
                "                       butterKnifeApt     : \"com.jakewharton:butterknife-compiler:$butterKnifeVersion\",\n" +
                "\n" +
                "                       autoValue          : \"com.google.auto.value:auto-value:1.4-rc2\",\n" +
                "                       autoParcelApt      : \"com.ryanharter.auto.value:auto-value-parcel:0.2.5\",\n" +
                "                       autoGsonApt        : \"com.ryanharter.auto.value:auto-value-gson:0.4.6\",\n" +
                "                       autoBundleApt      : \"com.github.yatatsu:autobundle-processor:$autoBundleVersion\",\n" +
                "                       icepickApt         : \"frankiesardo:icepick-processor:$icePickVersion\",\n" +
                "                       onActivityResultApt: \"com.vanniktech:onactivityresult-compiler:$onActivityResultVersion\",\n" +
                "\n" +
                "                       retrolambda        : \"net.orfjackal.retrolambda:retrolambda:2.5.0\",\n" +
                "                       errorprone         : \"com.google.errorprone:error_prone_core:2.0.15\",]\n" +
                "\n" +
                "    testDependencies = [junit                : \"junit:junit:4.12\",\n" +
                "                        mockito              : \"org.mockito:mockito-core:2.6.3\",\n" +
                "                        espresso             : \"com.android.support.test.espresso:espresso-core:$espressoVersion\",\n" +
                "                        espressoIntents      : \"com.android.support.test.espresso:espresso-intents:$espressoVersion\",\n" +
                "                        espessoContrib       : \"com.android.support.test.espresso:espresso-contrib:$espressoVersion\",\n" +
                "                        espessoWeb           : \"com.android.support.test.espresso:espresso-web:$espressoVersion\",\n" +
                "                        espessoIdlingResource: \"com.android.support.test.espresso:espresso-idling-resource:$espressoVersion\",\n" +
                "                        androidJUnitRunner   : \"com.android.support.test:runner:$testSupportLibVersion\",\n" +
                "                        androidJUnit4Rules   : \"com.android.support.test:rules:$testSupportLibVersion\",\n" +
                "\n" +
                "                        truth                : \"com.google.truth:truth:0.30\",\n" +
                "                        hamcrestLibrary      : \"org.hamcrest:hamcrest-library:1.4-atlassian-1\",\n" +
                "                        jsonAssert           : \"org.skyscreamer:jsonassert:$jsonAssertVersion\",\n" +
                "\n" +
                "                        mockWebServer        : \"com.squareup.okhttp3:mockwebserver:$okHttpVersion\",\n" +
                "                        restMockAndroid      : \"com.github.andrzejchm.RESTMock:android:0.1.1\",]\n" +
                "\n" +
                "    // jacoco config\n" +
                "    ignoredByJacoco = ['testbase', 'mocks']\n" +
                "\n" +
                "    moduleExcludes = ['base': [// Android framework related\n" +
                "                               '**/R*.class',\n" +
                "                               '**/BuildConfig.**',\n" +
                "                               '**/*Util*.**',\n" +
                "                               '**/Base**.**',\n" +
                "\n" +
                "                               // Android framework delegate implementation\n" +
                "                               '**/**DelegateImpl.**',\n" +
                "\n" +
                "                               // apt\n" +
                "                               '**/**AutoValue_**.**',\n" +
                "                               '**/**Factory.**',\n" +
                "\n" +
                "                               '**/**Config.**',\n" +
                "                               '**/**Builder.**',\n" +
                "\n" +
                "                               '**/**Module.**',\n" +
                "                               '**/**Module_**.**',\n" +
                "                               '**/**_Factory.**',\n" +
                "\n" +
                "                               '**/**_MembersInjector.**',],\n" +
                "                      'misc': [// Android framework related\n" +
                "                               '**/R*.class',\n" +
                "                               '**/BuildConfig.**',\n" +
                "                               '**/DbOpenHelper.class',\n" +
                "\n" +
                "                               // Android framework delegate implementation\n" +
                "                               '**/**DelegateImpl.**',\n" +
                "\n" +
                "                               // apt\n" +
                "                               '**/**AutoValue_**.**',\n" +
                "                               '**/**Factory.**',\n" +
                "\n" +
                "                               '**/**Config.**',\n" +
                "                               '**/**Builder.**',\n" +
                "\n" +
                "                               '**/**Module.**',\n" +
                "                               '**/**Module_**.**',\n" +
                "                               '**/**_Factory.**',\n" +
                "\n" +
                "                               '**/**_MembersInjector.**',],\n" +
                "                      'app' : ['**/R*.class',\n" +
                "                               '**/BuildConfig*']]\n" +
                "}\n";
        SourceUnit unit = SourceUnit.create("gradle", source);
        unit.parse();
        unit.completePhase();
        unit.convert();
        visitScriptCode(unit, new parseBuildGradle());
    }


    private static void visitScriptCode(SourceUnit source, GroovyCodeVisitor transformer) {
        source.getAST().getStatementBlock().visit(transformer);
        for (Object method : source.getAST().getMethods()) {
            MethodNode methodNode = (MethodNode) method;
            // System.out.println(methodNode.getName());
            //System.out.println(methodNode.getText());
            // System.out.println(methodNode.toString());
            methodNode.getCode().visit(transformer);
        }
    }

    static class parseBuildGradle extends CodeVisitorSupport {

        @Override
        public void visitMethodCallExpression(MethodCallExpression call) {
            super.visitMethodCallExpression(call);
       /*     if (isDependencyKey(call.getMethodAsString())) {
                L.l(call.getArguments().getText());
            }*/
            L.l(call.getMethodAsString());
            L.l(call.getArguments().getText());
        }
        private boolean isDependencyKey(String methodAsString) {
            return Config.DEPENDENCY_TAG.contains(methodAsString.toLowerCase());
        }
    }



}


