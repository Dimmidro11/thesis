# Отчёт о проведённом тестировании

## 1. Краткое описание
*"Проведено тестирование платежной формы веб-приложения. Проверены валидация полей, обработка ошибок и успешные сценарии оплаты."*

## 2. Тест-кейсы
- **Общее количество тест-кейсов:** 45
- **Успешные:** 31 (68.88% успешных)
- **Неуспешные:** 14 (31.11% неуспешных)


## 3. Баги
- [Issue 1](https://github.com/Dimmidro11/thesis/issues/1)
- [Issue 2](https://github.com/Dimmidro11/thesis/issues/2)
- [Issue 3](https://github.com/Dimmidro11/thesis/issues/3)
- [Issue 4](https://github.com/Dimmidro11/thesis/issues/4)
- [Issue 5](https://github.com/Dimmidro11/thesis/issues/5)
- [Issue 6](https://github.com/Dimmidro11/thesis/issues/6)
- [Issue 7](https://github.com/Dimmidro11/thesis/issues/7)
- [Issue 8](https://github.com/Dimmidro11/thesis/issues/8)
- [Issue 9](https://github.com/Dimmidro11/thesis/issues/9)
- [Issue 10](https://github.com/Dimmidro11/thesis/issues/10)
- [Issue 11](https://github.com/Dimmidro11/thesis/issues/11)
- [Issue 12](https://github.com/Dimmidro11/thesis/issues/12)
- [Issue 13](https://github.com/Dimmidro11/thesis/issues/13)

### Существенные проблемы
- **Валидация поля "Владелец"**  
  Необходимо в срочном порядке проверить валидацию на стороне frontend и backend
([Issue 8](https://github.com/Dimmidro11/thesis/issues/8), [Issue 10](https://github.com/Dimmidro11/thesis/issues/10), 
[Issue 11](https://github.com/Dimmidro11/thesis/issues/11), [Issue 12](https://github.com/Dimmidro11/thesis/issues/12),
[Issue 13](https://github.com/Dimmidro11/thesis/issues/13)).

  **Риски:**
    - Потенциальная уязвимость для SQL-инъекций или XSS-атак.
    - Некорректные данные попадают в БД.
- **Нарушена логика валидации на backend** ([Issue 2](https://github.com/Dimmidro11/thesis/issues/2),[Issue 7](https://github.com/Dimmidro11/thesis/issues/7),
[Issue 8](https://github.com/Dimmidro11/thesis/issues/8), [Issue 10](https://github.com/Dimmidro11/thesis/issues/10),
[Issue 11](https://github.com/Dimmidro11/thesis/issues/11), [Issue 12](https://github.com/Dimmidro11/thesis/issues/12),
[Issue 13](https://github.com/Dimmidro11/thesis/issues/13))  
  Backend пропускает большое количество невалидных данных, что в свою очередь несет разнообразные риски  
  **Риски:**:
    - **SQL-инъекции**
    - **XSS-атаки**
    - **Подмена параметров**
    - **Мусор в БД**
    - **Нарушение связей**
    - **Некорректная обработка**
    - **Падение сервиса**
    - **Хранение запрещённых данных**(персональные данные без согласия)


### Улучшения
- **Тестовые метки**  
  Внедрить уникальные `data-testid` (или аналоги) для ключевых элементов UI.  
  **Преимущества:**
    - Ускорение написания и поддержки UI-тестов.
    - Повышение стабильности тестов при изменениях вёрстки.
    - Снижение зависимости от CSS-селекторов.



---

## Интеграция с системами отчётности
Приложены автоматизированные отчёты:

- [Allure Report]()