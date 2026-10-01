require: slotfilling/slotFilling.sc
    module = sys.zb-common

theme: /

    state: Start
        q!: $regex</start>
        a: Привет! Я бот для заказа пиццы.
           Напишите, что хотите заказать, и я помогу оформить доставку.
        go!: /OrderPizza

    state: OrderPizza
        intent!: /OrderPizza
        a: Секунду, начат процесс оформления заказа ...
        # НЕТ go!: /ProcessOrder — слот-филлинг сам передаст управление,
        # когда все слоты будут заполнены.

    state: ProcessOrder
        # Этот стейт активируется автоматически, когда слот-филлинг завершён.
        # Но чтобы JAICP понял, что это продолжение, нужен специальный триггер.
        event!: slotFillingComplete
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
        # После изменения снова запускаем слот-филлинг? 
        # Нет — просто возвращаемся в ProcessOrder.
        go!: /ProcessOrder

    state: NoMatch
        event!: noMatch
        a: Извините, я не понял.