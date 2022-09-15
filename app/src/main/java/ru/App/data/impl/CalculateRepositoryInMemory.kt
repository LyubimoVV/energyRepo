package ru.tne.Application.data.impl

import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.App.R
import ru.App.data.CalculateRepository
import java.text.DecimalFormat

class CalculateRepositoryInMemory : CalculateRepository {

    override val events = MutableLiveData("")

    private var eventsCount: Int = 1

    private val textForEvents
        get() = events.value

    override fun getCount(): Int {
        return eventsCount - 1
    }

    override fun calculateVoltage(
        context: Context,
        voltage: String,
        voltageStandard: String
    ): Boolean {
        val voltageDouble = voltage.toDouble()
        val voltageStandardDouble = voltageStandard.toDouble()

        val unitVoltage =
            if (voltageStandard == "6" ||
                voltageStandard == "10" ||
                voltageStandard == "35" ||
                voltageStandard == "110"
            ) context.getString(R.string.kV) else context.getString(R.string.V)
        val df = DecimalFormat("#.##")
        val voltageStandardMin = voltageStandardDouble * (1 - voltageDeviation)
        val voltageStandardMinDec = df.format(voltageStandardMin)
        val voltageStandardMax = voltageStandardDouble * (1 + voltageDeviation)
        val voltageStandardMaxDec = df.format(voltageStandardMax)
        if (voltageDouble in voltageStandardMin..voltageStandardMax) return true

        events.value =
            textForEvents + "\n" + String.format(
                context.getString(R.string.problemVoltage),
                eventsCount++,
                ("$voltage $unitVoltage"),
                ("$voltageStandardMinDec - $voltageStandardMaxDec $unitVoltage")
            ) + "\n" + String.format(
                context.getString(R.string.voltage_less198_more242)
            )
        return false
    }

    override fun calculateFrequency(context: Context, frequency: String): Boolean {
        if (frequency.toDouble() in frequencyRangeHz) return true
        events.value = textForEvents + "\n" + String.format(
            context.getString(R.string.problemFrequency),
            eventsCount++,
            frequency
        ) + "\n" + String.format(
            context.getString(R.string.voltage_less198_more242)
        )
        return false
    }

    override fun calculateAsymmetry(context: Context, asymmetry: String): Boolean {
        val asymmetryDouble = asymmetry.toDouble()
        if (asymmetryDouble in asymmetryRange) return true
        if (asymmetryDouble in 2.0..4.0) {
            events.value = textForEvents + "\n" + String.format(
                context.getString(R.string.problemAsymmetry),
                eventsCount++,
                asymmetry
            ) + "\n" + String.format(
                context.getString(R.string.asymmetry2_4)
            )
        } else {
            events.value = textForEvents + "\n" + String.format(
                context.getString(R.string.problemAsymmetry),
                eventsCount++,
                asymmetry
            ) + "\n" + String.format(
                context.getString(R.string.asymmetryMore4)
            )
        }
        return false
    }

    override fun calculateNonSinus(
        context: Context,
        nonSinusoidality: String,
        voltageStandard: String
    ): Boolean {
        val nonSinusoidalityDouble = nonSinusoidality.toDouble()
        when (voltageStandard.toInt()) {
            220, 380 -> if (nonSinusoidalityDouble in nonSinusRange220and380) return true
            else {
                if (nonSinusoidalityDouble in 8.0..12.0) {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 12"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusLess)
                    )
                } else {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 12"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusMore)
                    )
                }
            }

            6, 10 -> if (nonSinusoidalityDouble in nonSinusRange6And10) return true
            else {
                if (nonSinusoidalityDouble in 5.0..8.0) {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 8"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusLess)
                    )
                } else {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 8"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusMore)
                    )
                }
            }
            35 -> if (nonSinusoidalityDouble in nonSinusRange35) return true
            else {
                if (nonSinusoidalityDouble in 4.0..6.0) {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 6"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusLess)
                    )
                } else {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 6"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusMore)
                    )
                }
            }
            else -> if (nonSinusoidalityDouble in nonSinusRange110And220) return true
            else {
                if (nonSinusoidalityDouble in 2.0..3.0) {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 3"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusLess)
                    )
                } else {
                    events.value = textForEvents + "\n" + String.format(
                        context.getString(R.string.problemNonSinus),
                        eventsCount++,
                        nonSinusoidality,
                        "0 - 3"
                    ) + "\n" + String.format(
                        context.getString(R.string.nonSinusMore)
                    )
                }
            }
        }
        return false
    }

    override fun reset() {
        events.value = ""
        eventsCount = 1
    }


    companion object {
        const val voltageDeviation = 0.1
        val frequencyRangeHz = 49.6..50.4
        val asymmetryRange = 0.0..2.0
        val nonSinusRange220and380 = 0.0..8.0
        val nonSinusRange6And10 = 0.0..5.0
        val nonSinusRange35 = 0.0..4.0
        val nonSinusRange110And220 = 0.0..2.0
    }


}