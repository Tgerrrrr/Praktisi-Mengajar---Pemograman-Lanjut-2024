import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

class Customer {
    String name;
    String type;
    String licensePlate;
    int queueNumber;
    String registrationDate;

    public Customer(String name, String type, String licensePlate, int queueNumber, String registrationDate) {
        this.name = name;
        this.type = type;
        this.licensePlate = licensePlate;
        this.queueNumber = queueNumber;
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Nama Customer: " + name + ", Tipe Mobil: " + type + ", Nomor Polisi: " + licensePlate + ", No Antrian: "
                + queueNumber + ", Tanggal Service: " + registrationDate;
    }

    public static Customer fromString(String data) {
        String[] parts = data.split(",");
        return new Customer(
                parts[0].split(": ")[1].trim(),
                parts[1].split(": ")[1].trim(),
                parts[2].split(": ")[1].trim(),
                Integer.parseInt(parts[3].split(": ")[1].trim()),
                parts[4].split(": ")[1].trim());
    }

    public String toFileFormat() {
        return "Antrian No: " + queueNumber + "\n" +
                "Nama: " + name + "\n" +
                "Tipe Mobil: " + type + "\n" +
                "Nomor Polisi: " + licensePlate + "\n" +
                "Registration Date: " + registrationDate + "\n";
    }

    public static Customer fromFileFormat(String data) {
        String[] parts = data.split(",");
        return new Customer(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                Integer.parseInt(parts[3].trim()),
                parts[4].trim());
    }
}

class EmptyQueueException extends Exception {
    public EmptyQueueException(String message) {
        super(message);
    }
}

class ServiceQueue {
    Queue<Customer> customerQueue;
    private int nextQueueNumber;
    private static final String FILE_NAME = "database.txt";

    public ServiceQueue() {
        customerQueue = new LinkedList<>();
        nextQueueNumber = 1;
        loadQueue();
    }

    public void enqueue(Customer customer) {
        customerQueue.add(customer);
        saveQueue();
    }

    public Customer dequeue() throws EmptyQueueException {
        Customer customer = customerQueue.poll();
        if (customer == null) {
            throw new EmptyQueueException("Antrian kosong, tidak ada customer untuk dikeluarkan.");
        }
        saveQueue();
        return customer;
    }

    public String displayQueue() {
        if (!customerQueue.isEmpty()) {
            StringBuilder display = new StringBuilder("Data customer yang tersedia:\n");
            for (Customer customer : customerQueue) {
                display.append(customer.toString()).append("\n");
            }
            return display.toString();
        } else {
            return "Antrian kosong.";
        }
    }

    public int getNextQueueNumber() {
        return nextQueueNumber++;
    }

    public void resetQueue() {
        customerQueue.clear();
        nextQueueNumber = 1;
        saveQueue();
    }

    private void saveQueue() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(String.valueOf(nextQueueNumber));
            writer.newLine();
            for (Customer customer : customerQueue) {
                writer.write(customer.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadQueue() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line = reader.readLine();
                if (line != null) {
                    nextQueueNumber = Integer.parseInt(line.trim());
                    while ((line = reader.readLine()) != null) {
                        Customer customer = Customer.fromFileFormat(line);
                        customerQueue.add(customer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ServiceGUI extends Frame {
    Label lbname, lbtype, lblicenseplate;
    TextField txtname, txttype, txtlicenseplate;
    Button btninsert, btnprocess, btndisplay, btnreset;
    TextArea displayArea;
    ServiceQueue serviceQueue;

    public ServiceGUI() {
        serviceQueue = new ServiceQueue();
        setLayout(null);

        lbname = new Label("Name : ");
        add(lbname);
        lbname.setBounds(50, 60, 100, 20);

        txtname = new TextField();
        add(txtname);
        txtname.setBounds(160, 60, 150, 20);

        lbtype = new Label("Car Type : ");
        add(lbtype);
        lbtype.setBounds(50, 100, 100, 20);

        txttype = new TextField();
        add(txttype);
        txttype.setBounds(160, 100, 150, 20);

        lblicenseplate = new Label("License Plate : ");
        add(lblicenseplate);
        lblicenseplate.setBounds(50, 140, 100, 20);

        txtlicenseplate = new TextField();
        add(txtlicenseplate);
        txtlicenseplate.setBounds(160, 140, 150, 20);

        btninsert = new Button("Insert");
        add(btninsert);
        btninsert.setBounds(50, 200, 100, 30);
        btninsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = txtname.getText().trim();
                String type = txttype.getText().trim();
                String licensePlate = txtlicenseplate.getText().trim();

                if (name.isEmpty() || type.isEmpty() || licensePlate.isEmpty()) {
                    displayArea.setText("Please fill out the form.");
                } else {
                    int queueNumber = serviceQueue.getNextQueueNumber();
                    String registrationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    Customer customer = new Customer(name, type, licensePlate, queueNumber, registrationDate);
                    serviceQueue.enqueue(customer);
                    displayArea.setText(name + " telah ditambahkan ke antrian.");
                    clearFields();
                }
            }
        });

        btnprocess = new Button("Process");
        add(btnprocess);
        btnprocess.setBounds(160, 200, 100, 30);
        btnprocess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Customer customer = serviceQueue.dequeue();
                    displayArea.setText("Processed: " + customer.toString());
                } catch (EmptyQueueException ex) {
                    displayArea.setText(ex.getMessage());
                }
            }
        });

        btndisplay = new Button("Display");
        add(btndisplay);
        btndisplay.setBounds(50, 240, 100, 30);
        btndisplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.setText(serviceQueue.displayQueue());
            }
        });

        btnreset = new Button("Reset");
        add(btnreset);
        btnreset.setBounds(160, 240, 100, 30);
        btnreset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serviceQueue.resetQueue();
                displayArea.setText("Antrian telah direset.");
            }
        });

        displayArea = new TextArea();
        add(displayArea);
        displayArea.setBounds(50, 300, 400, 150);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setSize(500, 500);
        setTitle("Service Queue Management");
        setVisible(true);
    }

    private void clearFields() {
        txtname.setText("");
        txttype.setText("");
        txtlicenseplate.setText("");
    }

    public static void main(String[] args) {
        new ServiceGUI();
    }
}