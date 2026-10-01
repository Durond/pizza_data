require: slotfilling/slotFilling.sc
    module = sys.zb-common

theme: /

    state: Start
        q!: $regex</start>
        a: Привет! Я бот для заказа пиццы.
           Напишите, что хотите заказать, и я помогу оформить доставку.
        # НЕТ go!: /OrderPizza — ждём, пока пользователь напишет

    state: OrderPizza
        intent!: /OrderPizza
        a: Секунду, начат процесс оформления заказа ...
        
        state: ProcessOrder
            a: Вы заказали: {{$parseTree._size ? $parseTree._size.name : "не указан"}} пиццу на {{$parseTree._base ? $parseTree._base.name : "не указана"}} основе с {{$parseTree._topping ? $parseTree._topping.name : "не указана"}}.
               Доставка по адресу: {{$parseTree._address ? $parseTree._address : "не указан"}}.
               Все верно?
            buttons:
                "Да" -> /CompleteOrder
                "Изменить" -> /ChangeOrder

    state: CompleteOrder
        image: https://aa9d726a-f32c-4d65-93b2-fde24d6221d0.selstorage.ru/1000218538/295880669/GFYjmuGsk3EapmsM.jpg
        a: Ваш заказ принят! Курьер уже в пути. 🍕
        EndSession:

    state: ChangeOrder
        intent!: /ChangeOrder
        a: Понял, меняю.
        go!: /OrderPizza

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Bye
        intent!: /пока
        a: Пока пока