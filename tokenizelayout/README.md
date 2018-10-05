SwitchLayout
===
[![Download](https://api.bintray.com/packages/iljaosintsev/TokenizeLayout/tokenizelayout/images/download.svg)](https://bintray.com/iljaosintsev/TokenizeLayout/tokenizelayout/_latestVersion)

`TokenizeLayout` – абстрактный `FameLayout` для манипулирования вложенными `View`.
Из всех потомков этого ViewGroup единовременно отображается только один, остальные – скрыты.
С помощью механизма токенизации каждому потомку присваивается уникальный идентификатор и смена видимости осуществляется с указанием этого токена.

`TokenizeLayout` реализует метод `changeToken(int)`, который показывает view с указанным токеном и скрывает все остальные.
Для указания, какое состояние считать скрытым для view используется `TokenizeLayoutParams` атрибут `layout_hided`,
который может принимать одно из двух значений: `invisible` и `gone`.
Метод `currentToken()` возвращает токен текущего видимого view.
ViewGroup указывает видимость потомков на этапе их добавления к разметке – переопределяет метод `addView(View, int)`.
За счет этого есть возможность указать, какой потомок должен отображаться по умолчанию.
Потомки этого класса должны предоставить реализацию абстрактных методов, определяющих способ токенизации.

`SwitchLayout` – реализует абстрактный `TokenizeLayout`.
Может содержать только три потомка, каждому из которых последовательно присваивается один из токенов: `CONTENT`, `ERROR` или `LOADING`.
Чаще всего потомки имеют площадь родителя и полностью перекрывают друг друга.
Методы `toContent()`, `toError()` и `toLoading()` позволяют сменить состояние пользовательского интерфейса.
Такой подход управления `View` на уровне `ViewGroup` не противоречит иерархии классов и снижает количество boilerplate кода.
Атрибут разметки `init` позволяет указать, какой потомок будет отображаться по умолчанию.

# Включение в проект

Добавьте репозиторий `jcenter()` к файлу проекта `build.gradle`

``` groovy
allprojects {
    repositories {
        jcenter()
    }
}
```

Укажите зависимость к модулю приложения, где X.Y.Z – семантическая версия библиотеки.

``` groovy
dependencies {
    ...
    implementation 'com.turlir.tokenizelayout:tokenizelayout:X.Y.Z'
}
```

# Использование

Сделайте `SwitchLayout` корневым в разметке экрана

``` xml
<com.turlir.tokenizelayout.SwitchLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/all_gist_switch"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:init="loading">

    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swipeLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_hided="gone">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </android.support.v4.widget.SwipeRefreshLayout>

    <com.turlir.abakgists.widgets.VectorTextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingTop="@dimen/half_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        android:paddingBottom="@dimen/half_margin"
        android:text="@string/main_network_error"/>

    <com.turlir.abakgists.widgets.VectorTextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingTop="@dimen/half_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        android:paddingBottom="@dimen/half_margin"
        android:text="@string/in_loading"/>

</com.turlir.tokenizelayout.SwitchLayout>
```

В этом случае, по умолчанию будет показан потомок с идентификатором `LOADING`,
то есть последний TextView с описанием процесса загрузки.
Для `SwipeRefreshLayout` (контента) состоянием скрыто будет `GONE`.
Согласованной сменой видимости потомков можно управлять программно.

``` java
public class MyFragment extends BaseFragment {

     @BindView(R.id.swipeLayout)
     SwipeRefreshLayout swipe;

     public void onDataLoaded() {
          // apply data
          swipe.toContent();
     }

     public void onLoadingStarted() {
          swipe.toLoading(); // block interaction
     }

     public void loadingError() {
          swipe.toError(); // show universal data
     }
}
```

# TODO

- Анимации при смене видимости потомков ViewGroup
- Методы из интерфейса `ChildManipulator` не должны быть видимы клиентскому коду