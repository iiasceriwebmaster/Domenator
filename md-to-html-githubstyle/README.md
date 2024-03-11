# Domenator

## Основная конечная точка API

Базовый URL для пользовательского API: `https://domenator.webfun.cf/`.
Все запросы и ответы отправляются по HTTPS.

## Все конечные точки и ожидаемые данные

`GET /get_lang`:

```json
{
  "message": "ok",
  "success": "1",
  "data": {
    "ru": {
      "app_name": "Domenator",
      "prompt_password": "Пароль",
      "action_sign_in": "Войдите в свой аккаунт",
      "action_sign_in_short": "Войти",
      "welcome": "Добро пожаловать!",
      "invalid_username": "Недействительное имя пользователя",
      "invalid_password": "Пароль должен быть &gt;5 символов",
      "login_failed": "Ошибка входа",
      "main_mid_text": "Чтобы начать добавлять данные о затраченном топливе, нажмите кнопку “Начать отчет”",
      "start_report": "Начать отчет",
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
      "no_records_yet": "У вас пока нет записей. Нажмите “+”, чтобы добавить данные о топливе ",
      "you_didn_t_add_any_photos_yet_press_button_to_add_them": "Вы еще не добавили ни одной фотографии, нажмите кнопку , чтобы добавить их",
      "user_profile": "Личный кабинет",
      "car_acceptance": "Приемка автомобиля",
      "driver_regulations": "Регламент водителя",
      "driver_help": "Помощь водителям",
      "logout": "Выйти",
      "licence_plate_nr": "Номер машины",
      "speedometer_value": "Показатели спидометра",
      "pick_date": "Выбрать дату",
      "date": "Дата",
      "fuel_quantity": "Количество топлива",
      "fuel_price": "Цена топлива",
      "change": "Изменить",
      "select_picture": "Выбрать изображение",
      "you_have_not_picked_image": "Вы не выбрали изображение",
      "name": "Имя",
      "surname": "Фамилия",
      "email": "Email",
      "date_of_birth": "Дата рождения",
      "are_you_sure_you_want_to_delete_this_image": "Вы уверены, что хотите удалить это изображение?",
      "delete": "Удалить",
      "cancel": "Отменить",
      "add": "Добавить",
      "please_fill_in_all_fields": "Заполните все поля",
      "add_report_info": "Добавить информацию об отчете",
      "add_car_info": "Добавить информацию об автомобиле",
      "car_reception": "Прием машины",
      "domenator_business_name": "DOMENATOR SP. Z O.O",
      "edit": "Редактировать"
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

`POST /login` *email *password:

```json
{
  "message": "ok",
  "success": "1",
  "data": {
    "firstName": "TestName",
    "lastName": "TestSurname",
    "birthDate": "11.05.2022",
    "email": "TestEmail"
  }
}
```

`GET /drivers_regulations`:

```json
{
  "message": "ok",
  "success": "1",
  "data": {
    "title": "RegulationsTitle",
    "content": "You should let your supervisor know in case of any: 1), 2), 3)..."
  }
}
```

`GET /drivers_help`:

```json
{
  "message": "ok",
  "success": "1",
  "data": {
    "title": "HelpTitle",
    "content": "In case of any trouble we are here to help..."
  }
}
```

`GET /get_notifications`:

```json
{
  "message": "ok",
  "success": "1",
  "data": [
    {
      "id": 1,
      "date": "04-05-2023",
      "title": "NotificationEntity Title",
      "content": "NotificationEntity Content"
    },
    {
      "id": 2,
      "date": "04-05-2023",
      "title": "NotificationEntity Title",
      "content": "NotificationEntity Content"
    }
  ]
}
```

`POST & PATCH(-> photos) /car_info` *id: 1, *date: "01.01.2023", *licencePlateNr: "KLH 150", *speedometerValue: 2569, *carPhotos: ["BASE64", "BASE64"] (парам мб и пустым), *documentPhotos: ["BASE64", "BASE64"] (парам мб и пустым):

```json
{
  "message": "ok",
  "success": "1",
  "data": {}
}
```

`GET /car_info` *id: 1:

```json
{
  "id": 1,
  "message": "ok",
  "success": "1",
  "data": {
    "date": "01.01.2023",
    "licencePlateNr": "KLH 150",
    "speedometerValue": 2569,
    "carPhotos": [
      "BASE64 string",
      "BASE64 string"
    ],
    "documentPhotos": [
      "BASE64 string",
      "BASE64 string"
    ]
  }
}
```

`POST /reports_info` *car_id: 1, *date: "01.01.2023", *fuelAmount: 123.5, *speedometerValue:2569, *fuelPrice: 1.5

```json
{
  "message": "ok",
  "success": "1",
  "data": {}
}
```

`GET /reports_info` *car_id: 1

```json
{
  "message": "ok",
  "success": "1",
  "data": [
    {
      "id": 1,
      "car_id": 1,
      "date": "01.01.2023",
      "fuelAmount": 123.5,
      "speedometerValue": 2569,
      "fuelPrice": 1.5
    },
    {
      "id": 2,
      "car_id": 1,
      "date": "02.01.2023",
      "fuelAmount": 65.3,
      "speedometerValue": 2631,
      "fuelPrice": 1.61
    }
  ]
}
```

[//]: # (`POST /report_info`:)

[//]: # (```)

[//]: # ({)

[//]: # (  "message": "ok",)

[//]: # (  "success": "1",)

[//]: # (  "data": {})

[//]: # (})

[//]: # (```)
