import java.util.LinkedList;
import java.util.Queue;

class Patient {
    String name;
    int age;

    public Patient(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class EmptyQueueException extends Exception {
    public EmptyQueueException(String message) {
        super(message);
    }
}

class HospitalQueue {
    Queue<Patient> patientQueue;

    public HospitalQueue() {
        patientQueue = new LinkedList<>();
    }

    public void enqueue(Patient patient) {
        patientQueue.add(patient);
        System.out.println(patient.name + " telah ditambahkan ke antrian.");
        System.out.println(" ");
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
            System.out.println("Pasien dalam antrian:");
            for (Patient patient : patientQueue) {
                System.out.println("Nama: " + patient.name + ", Umur: " + patient.age);
            }
        } else {
            System.out.println("Antrian kosong.");
        }
    }
}

public class Hospital {
    public static void main(String[] args) {
        HospitalQueue hospitalQueue = new HospitalQueue();

        // Menambahkan pasien ke antrian
        hospitalQueue.enqueue(new Patient("Tanner", 30));

        // Menampilkan antrian
        hospitalQueue.displayQueue();

        // Memproses antrian
        try {
            hospitalQueue.dequeue();
            hospitalQueue.dequeue();
            hospitalQueue.dequeue();
            hospitalQueue.dequeue(); // Menampilkan pesan bahwa antrian kosong
        } catch (EmptyQueueException e) {
            System.out.println(e.getMessage());
        }
    }
}
