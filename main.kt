import java.util.*

val mendeleevsTable = mapOf(
    "H" to 1.0,
    "He" to 4.0,
    "Li" to 7.0,
    "Be" to 9.0,
    "B" to 11.0,
    "C" to 12.0,
    "N" to 14.0,
    "O" to 16.0,
    "F" to 19.0,
    "Ne" to 20.0,
    "Na" to 23.0,
    "Mg" to 24.0,
    "Al" to 27.0,
    "Si" to 28.0,
    "P" to 31.0,
    "S" to 32.0,
    "Cl" to 35.5,
    "Ar" to 40.0,
    "Ca" to 40.0
)

// WARNING: Это рекурсивная функция
fun calculateMolecularWeight(f: String): Double {

    var i = 0              // Текущий индекс буквы
    val mi = f.length - 1  // Последний индекс в формуле
    var cEl = ""           // Текущий элемент
    var cMultR = ""        // Текущий множитель
    var cGroupWM = 0.0     // Рассчитанная молекулярная масса группы
    var weight = 0.0       // Молекулярная масса

    fun updateWeight() {

        // Если был элемент, суммируем массу
        if (cEl.isNotEmpty()) {
            weight +=
                    if (cMultR.isNotEmpty())
                        mendeleevsTable[cEl]!! * Integer.parseInt(cMultR)
                    else mendeleevsTable[cEl]!!
        }

        // Если же последней была группа, тогда суммируем ее
        else if (cGroupWM != 0.0) {
            weight +=
                    if (cMultR.isNotEmpty())
                        cGroupWM * Integer.parseInt(cMultR)
                    else cGroupWM
        }
    }

    fun calculateGroup() {
        var bi = mi

        // Ищем закрывающую скобку
        while (bi > i) {
            if (f[bi] == ')' || f[bi] == ']') {

                updateWeight()

                cEl = ""     // Сбрасываем элемент
                cMultR = ""  // Сбрасываем множитель

                cGroupWM = calculateMolecularWeight(f.substring(i + 1 until bi))
                i = bi  // Пропускаем группу
                break
            }

            bi--
        }
    }

    while (i <= mi) {

        if (f[i].isLetter()) {

            // Если заглавная буква, значит это начало
            //   нового химического элемента
            if (f[i].isUpperCase()) {

                updateWeight()

                cEl = "${f[i]}"  // Сбрасываем элемент и добавляем букву
                cMultR = ""      // Сбрасываем множитель
            }

            // Если маленькая буква, значит продолжаем читать
            else if (f[i].isLowerCase()) {
                cEl += f[i]
            }

        } else if (f[i].isDigit()) {
            cMultR += f[i]
        } else if (f[i] == '(' || f[i] == '[') {
            calculateGroup()
        }

        // Последняя буква
        if (i == mi) {
            updateWeight()
        }

        i++
    }

    return weight
}

fun main() {

    val input = Scanner(System.`in`)

    print("Введите формулу: ")
    val formula = input.next()

    val molecularWeight = calculateMolecularWeight(formula)

    println("Молекулярная масса: $molecularWeight")

    input.close()
}