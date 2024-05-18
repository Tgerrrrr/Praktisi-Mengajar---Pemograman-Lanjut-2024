import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public void displayQueue() {
        if (!patientQueue.isEmpty()) {
            System.out.println("Data pasien yang tersedia:");
            for (Patient patient : patientQueue) {
                System.out.println("Nama pasien: " + patient.name);
                System.out.println("Umur: " + patient.age + " tahun");
                System.out.println("No Antrian: " + patient.queueNumber);
                System.out.println("Tanggal Pendaftaran: " + patient.registrationDate);
                System.out.println();
            }
        } else {
            System.out.println("Antrian kosong.");
        }
    }

    public int getNextQueueNumber() {
        return nextQueueNumber++;
    }
}

public class Hospital {
    public static void main(String[] args) {
        HospitalQueue hospitalQueue = new HospitalQueue();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        while (true) {
            System.out.println("Selamat datang di International Hospital");
            System.out.println("1. Masukkan data Pasien");
            System.out.println("2. Tampilkan Data Pasien");
            System.out.println("3. Keluar");
            System.out.print("Pilih opsi: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Masukkan nama pasien: ");
                    String name = scanner.nextLine();
                    System.out.print("Masukkan umur pasien: ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    int queueNumber = hospitalQueue.getNextQueueNumber();
                    String registrationDate = dateFormat.format(new Date());
                    Patient newPatient = new Patient(name, age, queueNumber, registrationDate);
                    hospitalQueue.enqueue(newPatient);
                    break;
                case 2:
                    hospitalQueue.displayQueue();
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan layanan kami.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opsi tidak valid, silakan coba lagi.");
                    break;
            }
        }
    }
}
