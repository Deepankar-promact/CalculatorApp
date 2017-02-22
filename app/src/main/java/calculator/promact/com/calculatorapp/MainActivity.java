

package calculator.promact.com.calculatorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.EmptyStackException;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView displayResult;
    private EditText display;

    private String displayString = "";

    //Stack for mathematical operation
    private Stack<Integer> numberStack;
    private Stack<Character> operatorStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_ui);

        //Initializing stack
        numberStack = new Stack<>();
        operatorStack = new Stack<>();

        //Initializing views
        display = (EditText) findViewById(R.id.display);
        displayResult = (TextView) findViewById(R.id.display_result);

        //Hello!
        display.setText("HELLO");

    }

    /**
     * Triggers when user press a key.
     * @param view
     * Instance of view which triggered this function
     */
    public void keyPressed(View view) {
        Button button = (Button) view;

        //get the text from the button
        String key = button.getText().toString();

        //Check if the previous content of displayString is suspicious!!!
        if(displayString.equals("0")
                || displayString.equals("Format Error")
                || displayString.equals("Invalid Input")){
            displayString = "";
            displayResult.setText("");
        }

        if(!displayString.isEmpty() && displayString.charAt(displayString.length()-1) == '='){
            displayString = "";
        }
        //Suspect detected and resolved! phew!


        //Just another switch case
        switch (key){
            case "1":  displayString += "1";
                break;
            case "2":  displayString += "2";
                break;
            case "3":  displayString += "3";
                break;
            case "4":  displayString += "4";
                break;
            case "5":  displayString += "5";
                break;
            case "6":  displayString += "6";
                break;
            case "7":  displayString += "7";
                break;
            case "8":  displayString += "8";
                break;
            case "9":  displayString += "9";
                break;
            case "0":  displayString += "0";
                break;
            case "+":  displayString += "+";
                break;
            case "-":  displayString += "-";
                break;
            case "C":  displayString = "0";
                displayResult.setText("");
                break;
            case "=":
                //Time to solve the math!
                displayResult.setText(Integer.toString(solve()));
                break;
        }

        //Show the user input to the user.
        display.setText(displayString);
    }

    /**
     * Solves the math.
     * @return
     * solution
     */
    private int solve() {
        //Separate the numbers and operators in displayString
        parseDisplayString();


        int number = 0, result = 0;
        char operator = ' ';

        //Pop the first number before proceeding so that we can operate it
        //with other number when we know what operation to perform
        if(!numberStack.empty()){
            result = numberStack.pop();
        }


        try {
            while (!operatorStack.empty()) {

                number = numberStack.pop();
                operator = operatorStack.pop();

                Log.d("CALAPP", "Operator : " + operator + " | " + result + " " + operator + " " + number);

                switch (operator) {
                    case '+':
                        result += number;
                        break;
                    case '-':
                        result = number - result;
                        break;
                }
            }
        }catch(EmptyStackException e){
            displayString = "Invalid Input";

            //All the stack must be reset in case of error
            resetStack();

            Log.i("CALAPP",e.toString());
            return 0;
        }

        //Log.d("CALAPP", "Result : "+result);


        return result;
    }

    /**
     * Clears the stack
     */
    private void resetStack() {
        numberStack.clear();
        operatorStack.clear();
    }

    /**
     * Separate the numbers and operators and store them in corresponding stack
     */
    private void parseDisplayString() {
        String numberAsString = "";
        int number = 0, i = 0;

        //This flag is up when there is a minus symbol at the beginning
        boolean negativeNumber = false;

        if(displayString.isEmpty())
            displayString += "0";


        //raising the flag 'negativeNumber' if displayString starts with '-'
        if(displayString.charAt(0) == '-'){
            negativeNumber = true;
            i = 1;
        }else{
            //what if the first character is '+'. Resolved!
            i = displayString.charAt(0) == '+' ? 1 : 0 ;
        }

        Log.d("CALAPP","S="+displayString+"\nNN="+negativeNumber+"\ni="+i);



        displayString += "=";
        try {
            while (displayString.charAt(i) != '=') {
                if (displayString.charAt(i) != '+' && displayString.charAt(i) != '-')
                    numberAsString += displayString.charAt(i);
                else {

                    Log.d("CALAPP","NAS="+numberAsString+"\nString="+displayString.charAt(i)+"\ni="+i);
                    number = Integer.parseInt(numberAsString);

                    //Handling the negative number at the beginning if there
                    //is more than one number
                    if(negativeNumber) {
                        number *= -1;
                        negativeNumber = false;
                    }

                    numberAsString = "";
                    numberStack.push(number);
                    operatorStack.push(displayString.charAt(i));
                }
                i++;
            }

            number = Integer.parseInt(numberAsString);
            //Handling the negative number at the beginning if there
            //is only one number
            if(negativeNumber)
                number *= -1;

            numberStack.push(number);

        }catch (NumberFormatException e){
            displayString = "Format Error";

            //All the stack must be reset in case of error
            resetStack();

            Log.i("CALAPP","NUMBER FORMAT ERROR: "+e);
        }
    }

    //Found a bug. Report it to me! :)
}
