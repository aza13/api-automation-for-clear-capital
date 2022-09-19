import java.util.Arrays;

public class CreateInitials {


    public static void main(String[] args) {
        System.out.println(convertNameToInitials(""));
    }

    public static String convertNameToInitials(String name) {

//            int firstSpace = name.indexOf(' ');
//            int lastSpace = name.lastIndexOf(' ');
//            String firstName = name.substring(0, 1);
//            String middleName = name.substring(firstSpace , lastSpace);
//            String lastName = name.substring(lastSpace);
//
//        String initials = firstName + "." + middleName + "." + lastName + ".";
//        return initials;

        if (name.length() == 0)
            System.out.println("String should not be empty");
        String initials = "";
        System.out.print(Character.toUpperCase(
                name.charAt(0)) + ".");

        // Go through the  rest of the string and print the characters after spaces.
        for (int i = 1; i < name.length() - 1; i++)
            if (name.charAt(i) == ' ')
                initials = initials + Character.toUpperCase(name.charAt(i+1))+".";



        return initials;
    }






}
