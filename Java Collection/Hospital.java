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

class HospitalQueue {
    Queue<Patient> patientQueue;

    public HospitalQueue() {
        patientQueue = new LinkedList<>();
    }

    public void enqueue(Patient patient) {
        patientQueue.add(patient);
        System.out.println(patient.name + " telah ditambahkan ke antrian.");
    }

    public Patient dequeue() {
        Patient patient = patientQueue.poll();
        if (patient != null) {
            System.out.println(patient.name + " telah dikeluarkan dari antrian.");
        } else {
            System.out.println("Antrian kosong.");
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
        hospitalQueue.enqueue(new Patient("Chir", 25));
        hospitalQueue.enqueue(new Patient("Orb", 40));

        // Menampilkan antrian
        hospitalQueue.displayQueue();

        // Memproses antrian
        hospitalQueue.dequeue();
        hospitalQueue.dequeue();
        hospitalQueue.dequeue();
        hospitalQueue.dequeue(); // Menampilkan pesan bahwa antrian kosong
    }
}
