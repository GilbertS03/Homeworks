//Gilbert Isaac Salazar
//HW3
//This program takes in data from a file about employees and what kind of employees they are and displays their data in
//an organized manner. It is organized based on kind of employee they are to be easily readable.

import java.util.*;
import java.io.*;

abstract class Employee{
    private int _id;
    private String _name;

    //Default constructor
    public Employee(){
        _id = 0;
        _name = "";
    }
    //2-arg constructor
    public Employee(int id, String name){
        _id = id;
        _name = name;
    }
    public void setName(String name){_name = name;}
    public void setID(int id){_id = id;}
    public String getName(){return _name;}
    public int getID(){return _id;}
    public String toString(){
        return "Name: " + _name + " ID: " + _id;
    }
    public abstract double calculatePay();
    public abstract boolean equals(Employee e);
}

class FullTimeEmp extends Employee{
    private double _salary;
    //Default Constructor
    public FullTimeEmp(Employee e){
        super();
        _salary = 0;
    }
    //3-arg constructor
    public FullTimeEmp(int id, String name, double salary){
        super(id, name);
        _salary = salary;
    }
    public void setSalary(double salary){_salary = salary;}
    public double getSalary(){return _salary;}

    public String toString(){
        String x = super.toString();
        x = x + " Salary: " + _salary;
        return x;
    }

    public double calculatePay(){
        return _salary/24;
    }

    public boolean equals(Employee e){
        FullTimeEmp fte = (FullTimeEmp) e;
        if(getName().equals(fte.getName()) && getID() == fte.getID()
        && _salary == fte._salary){
            return true;
        }
        else
            return false;
    }
}

class PartTimeEmp extends Employee{
    private double _hours;
    private double _wage;
    //Default constructor
    public PartTimeEmp(){
        super();
        _hours = 0;
        _wage = 0;
    }
    //4-arg constructor
    public PartTimeEmp(int id, String name, double hours, double wage){
        super(id, name);
        _hours = hours;
        _wage = wage;
    }
    public void setHours(double hours){_hours = hours;}
    public void setWage(double wage){_wage = wage;}
    public double getHours(){return _hours;}
    public double getWage(){return _wage;}

    public String toString(){
        String x = super.toString();
        x = x + " Hours: " + _hours + " Wage: " + _wage;
        return x;
    }

    public double calculatePay(){
        double temp = _hours;
        //Gets rid of paid hours
        _hours -= temp;
        return (_wage*temp);
    }

    public boolean equals(Employee e){
        PartTimeEmp pte = (PartTimeEmp) e;
        if(getName().equals(pte.getName()) && getID() == pte.getID()
                && _hours == pte._hours && _wage == pte._wage){
            return true;
        }
        else
            return false;
    }
}

class Contractor extends Employee{
    private double _rate;
    private int _projects;
    //Default constructor
    public Contractor(){
        super();
        _rate = 0;
        _projects = 0;
    }
    //4-arg constructor
    public Contractor(int id, String name, int projects, double rate){
        super(id, name);
        _rate = rate;
        _projects = projects;
    }
    public void setRate(double rate){_rate = rate;}
    public void setProjects(int projects){_projects = projects;}
    public double getRate(){return _rate;}
    public int getProjects(){return _projects;}

    public String toString(){
        String x = super.toString();
        x = x + " Rate: " + _rate + " Projects: " + _projects;
        return x;
    }

    public double calculatePay(){
        int temp = _projects;
        //Gets rid of paid projects
        _projects -= temp;
        return (_rate*temp);
    }
    public boolean equals(Employee e){
        Contractor cont = (Contractor) e;
        if(getName().equals(cont.getName()) && getID() == cont.getID()
                && _projects == cont._projects && _rate == cont._rate){
            return true;
        }
        else
            return false;
    }
}

public class Main{
    //populate the arraylist of different types of employee objects
    public static void populate(ArrayList<Employee> employees){
        try{
            Scanner infile = new Scanner(new File("data.txt"));
            while(infile.hasNext()){
                //Checks employee type once per cycle or else bad inputs
                String[] employee = infile.nextLine().split(",");
                int type = Integer.parseInt(employee[0]);

                //If employee is a full-time employee
                if(type == 1){
                    int id = Integer.parseInt(employee[1]);
                    String name = employee[2] + " " + employee[3];
                    double salary = Double.parseDouble(employee[4]);
                    employees.add(new FullTimeEmp(id, name, salary));
                }
                //If employee is a part-time employee
                else if(type == 2){
                    int id = Integer.parseInt(employee[1]);
                    String name = employee[2] + " " + employee[3];
                    double hours = Double.parseDouble(employee[4]);
                    double wage = Double.parseDouble(employee[5]);
                    employees.add(new PartTimeEmp(id, name, hours, wage));
                }
                //If employee is a contractor
                else{
                    int id = Integer.parseInt(employee[1]);
                    String name = employee[2] + " " + employee[3];
                    int projects = Integer.parseInt(employee[4]);
                    double rate = Double.parseDouble(employee[5]);
                    employees.add(new Contractor(id, name, projects, rate));
                }
            }
        }
        catch(Exception e){
            System.out.println("Error in loading file");
            System.exit(-1);
        }
    }

    //Payroll for full-time employees
    public static void payrollFTE(ArrayList<Employee> employees){
        System.out.println("Full-time employee payroll: ");
        for(int i = 0; i < employees.size(); i++){
            if(employees.get(i) instanceof FullTimeEmp){
                //printf to get 2 decimal points in pay
                System.out.printf(employees.get(i).toString() + ", Pay: $%.2f",employees.get(i).calculatePay());
                System.out.println();
            }
        }
        //To not make it look so cramped
        System.out.println("__________________________________________________________________");
    }

    //Payroll for part-time employees
    public static void payrollPTE(ArrayList<Employee> employees){
        System.out.println("Part-time employee payroll: ");
        for(int i = 0; i < employees.size(); i++){
            if(employees.get(i) instanceof PartTimeEmp){
                //printf to get 2 decimal points in pay
                System.out.printf(employees.get(i).toString() + ", Pay: $%.2f",employees.get(i).calculatePay());
                System.out.println();
            }
        }
        //To not make it look so cramped
        System.out.println("__________________________________________________________________");
    }
    //Payroll for Contractors
    public static void payrollContractors(ArrayList<Employee> employees){
        System.out.println("Contractor payroll: ");
        for(int i = 0; i < employees.size(); i++){
            if(employees.get(i) instanceof Contractor){
                //printf to get 2 decimal points in pay
                System.out.printf(employees.get(i).toString() + ", Pay: $%.2f",employees.get(i).calculatePay());
                System.out.println();
            }
        }
        //To not make it look so cramped
        System.out.println("__________________________________________________________________");
    }

    //Menu function that returns the choice of the user
    public static int menu(Scanner scan){
        int choice;
        System.out.println("1. Print payroll for Full-Time Employees");
        System.out.println("2. Print payroll for Part-Time Employees");
        System.out.println("3. Print payroll for Contractors");
        System.out.println("4. Quit");
        choice = scan.nextInt();
        System.out.println("__________________________________________________________________");
        //Input validation
        while(choice > 4 || choice < 1){
            System.out.println("Invalid value, try again. ");
            System.out.println("__________________________________________________________________");
            System.out.println("1. Print payroll for Full-Time Employees");
            System.out.println("2. Print payroll for Part-Time Employees");
            System.out.println("3. Print payroll for Contractors");
            System.out.println("4. Quit");
            choice = scan.nextInt();
            System.out.println("__________________________________________________________________");
        }
        return choice;
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        ArrayList<Employee> employees = new ArrayList<>();
        populate(employees);
        System.out.println("Welcome!");
        int choice = menu(scan);

        while(choice != 4){
            if (choice == 1){
                payrollFTE(employees);
            }
            else if (choice == 2){
                payrollPTE(employees);
            }
            else if(choice == 3){
                payrollContractors(employees);
            }
            choice = menu(scan);
        }
        System.out.println("Goodbye!");
        System.out.println("__________________________________________________________________");
    }
}
