# Networking

* Возьмите любое API с картинками
  - Например [это](https://vk.com/dev/photos.search) или [это](https://unsplash.com/developers)
* Сделайте список с описанием картинок (и, например, с превьюшкам)
* По клику грузим и показываем выбранную картинку (в новой активити, например) в высоком качестве
* Приложение должно переживать перевороты и не перезапрашивать данные каждый раз

При открытии приложения грузится список фото с описанием из **интернета**.

По клику на элемент вы скачиваете **выбранную** картинку из интернета и показываете её.

Подумайте о пользователях. Если у вас белый экран без прогресса - от этого будет немного грустно, добавьте ProgressBar, например.

У вас ничего не утекает.

Можете получить бонусы за нормальную обработку ошибок, за работу приложения в условиях медленного интернета (да-да, это про сервисы), за кеширование.

Если вам не нравится парсить json вручную, то посмотрите на ![Jackson](https://github.com/FasterXML/jackson-databind) и ![GSON](https://github.com/google/gson)

Этот класс вам очень сильно понадобится: ![BitmapFactory](https://developer.android.com/reference/android/graphics/BitmapFactory)

![https://github.com/gerra/ITMO-Android-19/tree/master/Networking]
