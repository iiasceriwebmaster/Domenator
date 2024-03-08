
# Domenator

## API Main Endpoint

The base URL for the User API is `https://domenator.webfun.cf/`.
All requests and responses are sent over HTTPS.

## All endpoints and expected data

Example params:
*firstName
*lastName
*gender
*birthDate
*email
*password
*address
*profession
*isDeafInt(0/1)
*deafLevelInt(1 - лёгкий, 2 - средний, 3 - тяжёлый)
*languageOption(ru/ro)
*policyAgreement(0/1)


- `GET /get_lang`:
```
{
  "message": "ok",
  "success": "1",
  "data": {
    "ru": {
      "app_name": "Domenator",
      "prompt_email": "Эл. почта",
      "prompt_password": "Пароль",
      "action_sign_in": "Войти или зарегистрироваться",
      "action_sign_in_short": "Войти",
      "welcome": "Добро пожаловать!",
      "invalid_username": "Недействительное имя пользователя",
      "invalid_password": "Пароль должен быть более 5 символов",
      "login_failed": "Ошибка входа",
      "main_mid_text": "Чтобы начать добавлять данные о затраченном топливе, нажмите кнопку “Начать отчет”",
      "start_report": "Начать Отчет",
      "finish_report": "Завершить Отчет",
      "sign_in_into_your_account": "Войдите в свой аккаунт",
      "select_app_language": "Выберите язык приложения",
      "photos": "Фотографии",
      "documents": "Документы",
      "todo": "Список задач",
      "save": "Сохранить",
      "title_activity_nav": "АктивностьНавигации",
      "navigation_drawer_open": "Открыть панель навигации",
      "navigation_drawer_close": "Закрыть панель навигации",
      "nav_header_title": "Android Studio",
      "nav_header_subtitle": "android.studio@android.com",
      "nav_header_desc": "Заголовок навигации",
      "action_settings": "Настройки",
      "menu_home": "Домой",
      "menu_gallery": "Галерея",
      "menu_slideshow": "Слайд-шоу",
      "view_more": "Показать больше",
      "tap_to_see_more": "Нажмите, чтобы увидеть больше",
      "title": "Заголовок",
      "km_title": "002569 км",
      "title_date": "01.01.2023",
      "no_records_yet": "У вас еще нет записей. Нажмите кнопку «+», чтобы добавить данные о топливе.",
      "you_didn_t_add_any_photos_yet_press_button_to_add_them": "Вы еще не добавили ни одной фотографии, нажмите кнопку , чтобы добавить их",
      "user_profile": "Профиль пользователя",
      "car_acceptance": "Приемка автомобиля",
      "driver_regulations": "Правила для водителей",
      "driver_help": "Помощь водителю",
      "logout": "Выйти",
      "licence_plate_nr": "Гос. номер",
      "speedometer_value": "Значение спидометра",
      "pick_date": "Выбрать дату",
      "date": "Дата",
      "fuel_quantity": "Количество топлива",
      "fuel_price": "Цена топлива",
      "change": "Сдача",
      "select_picture": "Выбрать изображение",
      "you_have_not_picked_image": "Вы не выбрали изображение",
      "name": "Имя",
      "surname": "Фамилия",
      "email": "Эл. почта",
      "date_of_birth": "Дата рождения",
      "are_you_sure_you_want_to_delete_this_image": "Вы уверены, что хотите удалить это изображение?",
      "delete": "Удалить",
      "cancel": "Отменить",
      "add": "Добавить",
      "please_fill_in_all_fields": "Заполните все поля",
      "add_report_info": "Добавить информацию об отчете",
      "add_car_info": "Добавить информацию об автомобиле"
    },
    "ro": {
      "app_name": "Domenator",
      "prompt_email": "Email..............................."
    },
    "en": {
      "app_name": "Domenator",
      "prompt_email": "mail..............................."
    }
  }
}
```

- `POST /login`:
```
{
  "message": "ok",
  "success": "1",
  "data": {
    "firstName":"TestName",
    "lastName":"TestSurname",
    "birthDate":"11.05.2022",
    "email":"TestEmail"
  }
}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```

- `GET POST /endpoint`:
```
{}
```
