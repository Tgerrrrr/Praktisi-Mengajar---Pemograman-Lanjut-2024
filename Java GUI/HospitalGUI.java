import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

class Patient {
    String name;
    int age;
    int queueNumber;
    String registrationDate;

    public Patient(String name, int age, int queueNumber, String registrationDate) {
        this.name = name;
        this.age = age;
        this.queueNumber = queueNumber;
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Nama pasien: " + name + ", Umur: " + age + " tahun, No Antrian: " + queueNumber
                + ", Tanggal Pendaftaran: " + registrationDate;
    }
}

class EmptyQueueException extends Exception {
    public EmptyQueueException(String message) {
        super(message);
    }
}

class HospitalQueue {
    Queue<Patient> patientQueue;
    private int nextQueueNumber;

    public HospitalQueue() {
        patientQueue = new LinkedList<>();
        nextQueueNumber = 1;
    }

    public void enqueue(Patient patient) {
        patientQueue.add(patient);
        System.out.println(patient.name + " telah ditambahkan ke antrian.");
    }

    public Patient dequeue() throws EmptyQueueException {
        Patient patient = patientQueue.poll();
        if (patient != null) {
            System.out.println(patient.name + " telah dikeluarkan dari antrian.");
        } else {
            throw new EmptyQueueException("Antrian kosong, tidak ada pasien untuk dikeluarkan.");
        }
        return patient;
    }

    public String displayQueue() {
        if (!patientQueue.isEmpty()) {
            StringBuilder display = new StringBuilder("Data pasien yang tersedia:\n");
            for (Patient patient : patientQueue) {
                display.append(patient.toString()).append("\n");
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
        patientQueue.clear();
        nextQueueNumber = 1;
    }
}

// TAMPILAN GUI
public class HospitalGUI extends JFrame {
    private HospitalQueue hospitalQueue;
    private JTextField nameField;
    private JTextField ageField;
    private JTextArea displayArea;
    private SimpleDateFormat dateFormat;

    public HospitalGUI() {
        hospitalQueue = new HospitalQueue();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        setTitle("International Hospital Queue System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // components
        nameField = new JTextField(15);
        ageField = new JTextField(5);
        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);

        JButton addButton = new JButton("Masukkan data Pasien");
        JButton displayButton = new JButton("Tampilkan Data Pasien");
        JButton processButton = new JButton("Proses Antrian");
        JButton resetButton = new JButton("Reset Antrian");

        // action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                int queueNumber = hospitalQueue.getNextQueueNumber();
                String registrationDate = dateFormat.format(new Date());
                Patient newPatient = new Patient(name, age, queueNumber, registrationDate);
                hospitalQueue.enqueue(newPatient);
                saveQueueToFile();
                nameField.setText("");
                ageField.setText("");
                JOptionPane.showMessageDialog(null, "Pasien telah ditambahkan ke antrian.");
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText(hospitalQueue.displayQueue());
            }
        });

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Patient processedPatient = hospitalQueue.dequeue();
                    saveQueueToFile();
                    JOptionPane.showMessageDialog(null, "Pasien yang diproses: " + processedPatient.name);
                } catch (EmptyQueueException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hospitalQueue.resetQueue();
                saveQueueToFile();
                displayArea.setText("");
                JOptionPane.showMessageDialog(null, "Antrian telah direset.");
            }
        });

        // Layout setup
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nama:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Umur:"));
        inputPanel.add(ageField);
        inputPanel.add(addButton);

        JPanel controlPanel = new JPanel();
        controlPanel.add(displayButton);
        controlPanel.add(processButton);
        controlPanel.add(resetButton);

        JPanel displayPanel = new JPanel();
        displayPanel.add(new JScrollPane(displayArea));

        add(inputPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(displayPanel, BorderLayout.SOUTH);
    }

    private void saveQueueToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("queue.txt", false))) {
            for (Patient patient : hospitalQueue.patientQueue) {
                writer.println(
                        patient.name + "," + patient.age + "," + patient.queueNumber + "," + patient.registrationDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadQueueFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("queue.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                int age = Integer.parseInt(data[1]);
                int queueNumber = Integer.parseInt(data[2]);
                String registrationDate = data[3];
                Patient patient = new Patient(name, age, queueNumber, registrationDate);
                hospitalQueue.enqueue(patient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HospitalGUI gui = new HospitalGUI();
                gui.setVisible(true);
                gui.loadQueueFromFile();
            }
        });
    }
}
