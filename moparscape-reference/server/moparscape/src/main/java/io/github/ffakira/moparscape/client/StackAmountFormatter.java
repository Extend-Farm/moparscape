package io.github.ffakira.moparscape.client;

final class StackAmountFormatter {

    private StackAmountFormatter() {
    }

    static String formatStackAmount(int amount) {
        String amountText = String.valueOf(amount);
        for (int split = amountText.length() - 3; split > 0; split -= 3) {
            amountText = amountText.substring(0, split) + "," + amountText.substring(split);
        }

        if (amountText.length() > 8) {
            return " @gre@" + amountText.substring(0, amountText.length() - 8) + " million @whi@(" + amountText + ")";
        }
        if (amountText.length() > 4) {
            return " @cya@" + amountText.substring(0, amountText.length() - 4) + "K @whi@(" + amountText + ")";
        }
        return " " + amountText;
    }
}
