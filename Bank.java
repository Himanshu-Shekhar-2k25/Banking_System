import java.util.Scanner;
import java.util.HashMap;
import java.util.Stack;

import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;


class Transaction
{
    int txn_id;
    String txn_type; // debit or credit or transfer
    Double txn_amount;
    Customer sent_to;

    public Transaction(int id, String type, double amt, Customer receiver)
    {
        this.txn_id = id;
        this.txn_type = type;
        this.txn_amount = amt;
        this.sent_to = receiver;
    }
}


class Customer
{
    int acc_no;
    String date;
    double balance;
    String uname;
    String pwd;
    Stack<Transaction> stack;
    Stack<Transaction> stack_helper;

    public Customer(int acc_no,String date,double balance, String uname, String pwd)
    {
        this.acc_no = acc_no;
        this.date = date;
        this.balance = balance;
        this.uname = uname;
        this.pwd = pwd;
        this.stack = new Stack<Transaction>();
        this.stack_helper = new Stack<Transaction>();
    }

    public void deposit(double amount)
    {
        this.balance = this.balance + amount;
        create_txn(amount,"Credit", this);
        System.out.println("Amount deposited successfully.");
    }

    public void withdraw(double amount)
    {
        if(this.balance < amount)
        {
            System.out.println("Insufficient balance. Transaction aborted");
            return;
        }
        this.balance = this.balance - amount;
        create_txn(amount,"Debit", this);
        System.out.println("Amount withdrawn successfully");
    }


    public void create_txn(double amount, String type, Customer receiver)
    {
        int txn_id = (int)(Math.random() * 100000000);
        Transaction ob = new Transaction(txn_id,type,amount,receiver);
        this.stack.push(ob);
        System.out.println("<---  Transaction details  --->");
        System.out.println("    ID : " + txn_id);
        System.out.println("    Type : " + type);
        System.out.println("    Amount : " + amount);
        System.out.println("    Receipient Username : " + receiver.uname);
        System.out.println("    Receipient Account Number: " + receiver.acc_no);
    }

    public void transfer(double amount, Customer receiver)
    {
        if(this.balance < amount)
        {
            System.out.println("Insufficient Balance. Transaction aborted");
            return;
        }
        this.balance = this.balance - amount;
        receiver.balance = receiver.balance + amount;
        create_txn(amount,"Transfer",receiver);
        System.out.println("Transfer successful");
    }

    public void print_Details()
    {
        System.out.println("<---    Customer details    --->");
        System.out.println("Acc No : "+ this.acc_no);
        System.out.println("Username : "+this.uname);
        System.out.println("Date of opening : " + this.date);
        System.out.println("Current Balance : "+ this.balance);
        System.out.println("Total number of transactions : "+stack.size());
    }

    public void last_n_txn(int n)
    {
        Transaction temp;
        for(int i=0;i<n;i++)
        {
            temp = stack.pop();
            stack_helper.push(temp);
            System.out.println("Transaction"+(i+1));
            System.out.println("Transaction ID : "+temp.txn_id);
            System.out.println("Transaction type : "+temp.txn_type);
            System.out.println("Trasaction amount : "+temp.txn_amount);
            System.out.println("Receipient Username : "+temp.sent_to.uname);
            System.out.println("Receipient Account : "+temp.sent_to.acc_no);
        }
        while(!stack_helper.isEmpty())
        {
            stack.push(stack_helper.pop());
        }
    }
}

class Bank
{
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);  
        HashMap<Integer,Customer> map = new HashMap<>();
        int outer_choice = -1;

        while(outer_choice != 4)
        {
        System.out.println("\n<--- Welcome to our E - Banking System --->");
            System.out.println("1. Open an account");
            System.out.println("2. Close an existing account");
            System.out.println("3. Manage an existing account");
            System.out.println("4. Exit");
            System.out.println("\nEnter your choice");
            outer_choice = sc.nextInt();

            switch(outer_choice)
            {
                case 1:
                    {
                        int acc_no = (int)(Math.random()*100000000);

                        System.out.println("Welcome , your Account Number is = "+acc_no);
                        String date="";
                        //date -----------------!!!!!
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        Calendar cal = Calendar.getInstance();
                        date = formatter.format(cal.getTime());

                        System.out.println("Enter you Username");
                        String uname;
                        uname = sc.next();  

                        System.out.println("Enter your Password");
                        String pwd = sc.next();

                        System.out.println("Enter your initial deposit");
                        double val = sc.nextDouble();

                        Customer obj = new Customer(acc_no, date, val, uname,pwd);
                        map.put(acc_no,obj);
                        break;
                    }
                case 2:
                    {
                        System.out.println("Enter the account number to close");
                        int acc_no = sc.nextInt();
                        while(!map.containsKey(acc_no))
                        {
                            System.out.println("Invalid Account number");
                            acc_no = sc.nextInt();
                        }
                        map.remove(acc_no);
                        System.out.println("Account deleted successfully");
                        break;
                    }
                case 3:
                    {
                        int inner_choice = -1;
                        int flag = 0;
                        Customer curr = null;
                        while(inner_choice != 6)
                        {
                            int acc_no = -1;
                            String pwd;
                            if(flag == 0)
                            {
                                System.out.println("< -- LOGIN HERE -- >");
                                while(!map.containsKey(acc_no))
                                {
                                    System.out.println("Enter account number");
                                    acc_no = sc.nextInt();
                                    System.out.println("Enter password");
                                    pwd = sc.next();
                                    if(map.containsKey(acc_no) && pwd.equals(map.get(acc_no).pwd))
                                    {
                                        curr = map.get(acc_no);
                                        System.out.println("Logged in successfully");
                                    }
                                    else
                                    {
                                        System.out.println("Wrong credentials. Please try again");
                                        acc_no = -1;
                                    }
                                }
                            }

                            System.out.println("\n1. Print Account Details");
                            System.out.println("2. Deposit Money");
                            System.out.println("3. Withdraw Money");
                            System.out.println("4. Transfer Money");
                            System.out.println("5. Last n transactions");
                            System.out.println("6. Exit to Main Menu");

                            System.out.println("Enter your choice");
                            inner_choice = sc.nextInt();

                            switch(inner_choice)
                            {
                                case 1:
                                    {
                                        curr.print_Details();
                                        flag = 2;
                                        break;
                                    }
                                case 2:
                                    {
                                        double val;
                                        System.out.println("Enter the value to be deposited");
                                        val = sc.nextDouble();
                                        curr.deposit(val);
                                        flag = 2;
                                        break;
                                    }
                                case 3:
                                    {
                                        double val;
                                        System.out.println("Enter the value to withdraw");
                                        val = sc.nextDouble();
                                        curr.withdraw(val);
                                        flag=2;
                                        break;
                                    }
                                case 4:
                                    {
                                        System.out.println("Enter the amount of money to be transferred");
                                        double val = sc.nextDouble();

                                        System.out.println("Enter the receipient account number");
                                        int r_accNo = sc.nextInt();
                                        while(!map.containsKey(r_accNo))
                                        {
                                            System.out.println("Account Number does not exist. Retry");
                                            r_accNo = sc.nextInt();

                                        }
                                        curr.transfer(val,map.get(r_accNo));
                                        flag = 2;
                                        break;
                                    }
                                case 5:
                                    {
                                        System.out.println("Enter the number of last transactions you wish to see details of \n");
                                        int n = sc.nextInt();
                                        curr.last_n_txn(n);
                                        flag = 2;
                                        break;
                                    }
                                case 6:
                                    {
                                        flag = 1;
                                        break;
                                    }
                                default:
                                    {
                                        flag = 2;
                                        System.out.println("Invalid choice entered");
                                        break;
                                    }
                            }
                        }
                        break;
                    }
                case 4:
                    {
                        System.out.println("Thanks for using our E - Banking services. \nHave a great day ahead");
                        return;
                    }
                default:
                    {
                        System.out.println("Wrong choice entered. Please try again");
                    }
            }
        }
    }
}