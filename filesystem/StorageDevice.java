package filesystem;
import java.util.*;

class StorageDevice {

    // Look-up table for the blocks allocated per fileID
    private HashMap<Long, ArrayList<Long>> files;

    // Contains indicies for blocks being used for a file
    private Set<Long> freeBlocks;
    
    // Capacity and block size are stored in bytes
    private long capacity;
    private long blockSize;

    private long totalBlockCount;
    private long blocksRemaining;

    /*
        newCapacity: size of storage device in GB
        newBlockSize: size of each block in KB
    */
    public StorageDevice(int newCapacity, int newBlockSize) {
        // Max capacity set at 100GB
        newCapacity = newCapacity > 100 ? 100 : newCapacity;

        // Convert MB to bytes
        capacity = newCapacity * 1024 * 1024;

        // KB to bytes
        blockSize = newBlockSize * 1024;

        totalBlockCount = capacity / blockSize;

        // All blocks are available initially
        blocksRemaining = totalBlockCount;

        files = new HashMap<>();
        freeBlocks = new HashSet<>();

        // Initialize set of available blocks
        for(long i = 0; i < totalBlockCount; i++) {
            freeBlocks.add(i);
        }
    }

    /*
        fileID: the unique ID of a file.
        fileSize: size of file in bytes

        Allocates blocks for the file using a linear scan.
    */
    public ArrayList<Long> saveFile(long fileID, long fileSize) {
        // Number of blocks needed to store the file
        long blocksNeeded = (long) Math.ceil((double) fileSize / blockSize);

        // Exception handling
        // File already exists
        if(files.containsKey(fileID)) {
            System.out.println("File has already been saved.");
            return new ArrayList<>();
        }
        // File is too large for storage capacity
        else if(blocksNeeded > blocksRemaining) {
            System.out.println("File size is larger than remaining storage capacity.");
            return new ArrayList<>();
        }

        // Holds the indicies of each block allocated for file
        ArrayList<Long> allocatedBlocks = new ArrayList<>();

        // Allocate blocks for file
        for(long freeBlock : freeBlocks) {
            if(allocatedBlocks.size() == blocksNeeded) {
                break;
            }
            allocatedBlocks.add(freeBlock);
        }

        // Remove allocated blocks from set of free blocks
        for(long blockToAllocate : allocatedBlocks) {
            freeBlocks.remove(blockToAllocate);
        }
        
        // Save file and its blocks in look-up table
        files.put(fileID, allocatedBlocks);

        // Update remaining blocks
        blocksRemaining -= allocatedBlocks.size();

        return allocatedBlocks;
    }

    /*
        fileID: the unique id of the file we want to delete.

        If file exists, delete the blocks allocated for the file.
    */
    public void deleteFile(long fileID) {
        if(files.containsKey(fileID)) {
            ArrayList<Long> blocksToRemove = files.get(fileID);

            // Add the blocks of our deleted file back to the free blocks set
            for(long block : blocksToRemove) {
                freeBlocks.add(block);
            }
    
            files.remove(fileID);
            blocksRemaining += blocksToRemove.size();
        }
    }

    /*
        fileID: the unique id of the file we want to delete.

        If file exists, return the blocks associated to the file.
    */
    public ArrayList<Long> getFile(long fileID) {
        if(files.containsKey(fileID)) {
            return files.get(fileID);
        }
        System.out.println("File " + fileID + " does not exist.");
        return new ArrayList<>();
    }

}