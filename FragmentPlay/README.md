# Навигация

[Как примерно должно выглядеть](https://github.com/gerra/ITMO-Android-19/blob/master/Navigation/2019-10-14_01-12-49_CaptureRecorder.mp4)
Что хотим увидеть:
* 3 таба.
* В каждом – свой бэкстэк.
* Всё должно быть на фрагментах (с одной активити).
* Кнопка “Назад” в тулбаре и системная “Назад” ведут себя одинаково: если в стеке текущего таба есть фрагменты, то выкидываем один. Иначе – или выход из приложения, или возврат на предыдущий таб с непустым стеком (за это можно получить бонус).
* Для горизонтальной ориентации другая верстка (по ссылке – просто пример, можете сделать что-то другое). Стейт при повороте не должен теряться.

Бонусы:
* Анимации (желательно что-то посложнее fade in и fade out).
* Как и писал выше, “умная” навигация. Приложение не закрывается по “back”, пока есть хоть один таб с непустым стеком.

Может пригодиться:
* [getChildFragmentManager()](https://developer.android.com/reference/android/app/Fragment.html#getChildFragmentManager()), 
* [show()](https://developer.android.com/reference/androidx/fragment/app/FragmentTransaction#show(androidx.fragment.app.Fragment)), 
* [hide()](https://developer.android.com/reference/androidx/fragment/app/FragmentTransaction#hide(androidx.fragment.app.Fragment))
* [setArguments()](https://developer.android.com/reference/android/app/Fragment.html#setArguments(android.os.Bundle))  (за использование кастомных конструкторов, а не setArguments(), будем отправлять переделывать с минусами)
* [getSupportActionBar()](https://developer.android.com/reference/android/support/v7/app/AppCompatActivity#getsupportactionbar)
* [onSupportNavigateUp()](https://developer.android.com/reference/android/support/v7/app/AppCompatActivity#onsupportnavigateup)
* [onBackPressed()](https://developer.android.com/reference/android/support/v4/app/FragmentActivity.html#onBackPressed())
