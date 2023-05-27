package com.example.codeinterpretator.ui.theme

val DRAWER_TITLE = "Коснитесь, чтобы добавить"
val TITLE_DECLARATION_BLOCK = "Декларация + присваивание"
val TITLE_ASSIGNMENT_BLOCK = "Присваивание"
val TITLE_ARRAY_ASSIGNMENT_BLOCK = "Декларация массива"
val TITLE_INPUT_BLOCK = "Ввод"
val TITLE_OUTPUT_BLOCK = "Вывод"
val TITLE_OUTPUT_IFELSE = "Условное ветвление"

val BLOCKLABEL_VARIABLE = "Variable"
val BLOCKLABEL_NAME = "Name"
val BLOCKLABEL_VALUE = "Value"
val BLOCKLABEL_NAMES = "Names"
val BLOCKLABEL_VALUES = "Values"
val BLOCKLABEL_OUTPUT = "Output"
val BLOCKLABEL_EXPRESSION = "Expression"

val BLOCKTEXT_PRINT = "print"
val BLOCKTEXT_READ = "read"
val BLOCKTEXT_IF = "if"
val BLOCKTEXT_ELSE = "else"

val TYPENAME_INT = "Int"
val TYPENAME_STRING = "String"
val TYPENAME_BOOL = "Bool"
val TYPENAME_DOUBLE = "Double"
val TYPENAME_CHAR = "Char"
val EQUALSIGN = "="

val INPUT_SEND = "Отправить"

const val ERROR_INVALID_ARRAY_NAME = "Указан некорректное название массива для переменной: "
const val ERROR_INVALID_INDEX = "Указан некорректный индекс для переменной: "
const val ERROR_NULL_VARIABLE = "Следующая переменная имеет значение null:"
const val ERROR_INSUFFICIENT_OPERANDS = "Выражение не имеет достаточно переменных!"
const val ERROR_ADDITION_NON_NUMERIC = "Невозможная операция: Сложение нечисловых переменных."
const val ERROR_SUBTRACTION_NON_NUMERIC = "Невозможная операция: Вычитание нечисловых переменных."
const val ERROR_MULTIPLICATION_NON_NUMERIC =
    "Невозможная операция: Умножение нечисловых переменных."
const val ERROR_DIVISION_NON_NUMERIC = "Невозможная операция: Деление нечисловых переменных."
const val ERROR_MODULUS_NON_NUMERIC = "Невозможная операция: Взятие % для нечисловых переменных."
const val ERROR_DIVISION_BY_ZERO = "Невозможная операция: Деление на 0."
const val ERROR_COMPARISON_NON_NUMERIC = "Невозможная операция: Сравнение нечисловых переменных."
const val ERROR_COMPARISON_DIFFERENT_TYPES = "Сравнение переменных разного типа невозможно!"
const val ERROR_INVALID_OPERATOR = "Оператор инвалид: "
const val ERROR_INSUFFICIENT_OPERATORS = "Выражение не имеет достаточно операторов!"
const val ERROR_INT_TO_CHAR_INSTEAD_OF_CHAR_TO_INT =
    "Попытка применить следующее действие к Int Char (Может быть, вы имели в виду, Char Int?): "

const val ERROR_CHAR_OUT_OF_RANGE =
    "Значение выражения вышло за границы Char и было конвертировано в Int."


const val ERROR_NOT_BOOLEAN_TYPE = "Выражение имеет небулевый тип"
const val ERROR_DIFFERENT_TYPES_ARRAY_ASSIGNMENT = "Нельзя присваивать массивы разных типов!"
const val ERROR_STOP_FULL_ARRAY_ASSIGNMENT =
    "Полное присваивание массива возможно только на этапе объявления!"
const val ERROR_ARRAY_UNCOMPATIBLE_TYPES = "Несоответствующие типы массивов и значений: "
const val ERROR_UNDECLARED_VARIABLE_ASSIGNMENT =
    "Попытка присвоения значений необъявленной переменной: "
const val ERROR_ASSIGNING_EMPTY_EXPRESSION = "Попытка присвоения пустого выражения"
const val ERROR_NO_VARIABLE_NAME = "Вы никак не назвали переменную!"
const val ERROR_REDECLARING_VARIABLES = "Редекларация переменной невозможна"
const val ERROR_USING_KEYWORDS_IN_VARIABLE_NAME =
    "Пожалуйста, не используйте ключевые слова в качестве названий переменных"
const val ERROR_CONTAINING_RESTRICTED_SYMBOLS = "Название переменной содержит запрещённые символы!"
const val ERROR_DIFFERENT_TYPES_VARIABLES_ASSIGNMENT =
    "Некорректные типы операндов при присваивании: "
const val ERROR_TOO_MANY_VARIABLES_FOR_ARRAY =
    "Вы ввели слишком много переменных при создании массива!"
const val ERROR_ARRAY_SIZE_NOT_INTEGER = "Размер массива имеет нецелочисленный тип!"
const val ERROR_ARRAY_SIZE_IS_NEGATIVE = "Размер массива имеет отрицательное значение!"
const val ERROR_READING_ARRAY_AS_A_WHOLE = "Нельзя считать весь массив сразу"
const val ERROR_NO_NAME_ENTERED = "Введите имя считываемой переменной"
const val ERROR_DIFFERENT_ARRAY_SIZES =
    "Один из массивов имеет размер больше второго. Было присвоено следующее количество первых подходящих элементов: "

const val ERROR_INCORRECT_ARRAY_NAME = "Указан некорректное название массива для переменной: "
const val ERROR_INCORRECT_INDEX = "Указан некорректный индекс для переменной: "
const val ERROR_MODULUS_BY_ZERO = "Нельзя брать остаток от нуля!"
const val ERROR_ARRAY_TYPE_NOT_DECLARED = "Тип массива не указан"

const val UNDEFINED_TYPE = "Undefined Type"