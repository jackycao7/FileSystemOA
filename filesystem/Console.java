package FileSystemOA.filesystem;

public class Console {
    
    public static void main(String args[]) {
        // Example test cases
        StorageDevice storage = new StorageDevice(1, 1);

        for(int i = 0; i < 1024; i++) {
            storage.saveFile(i, 1024);
        }

        // Should fail
        storage.saveFile(1025, 1);

        for(int i = 0; i < 1024; i += 2) {
            storage.deleteFile(i);
        }

        // Should fragment
        storage.saveFile(1025, 1024 * 50);

    }
}
