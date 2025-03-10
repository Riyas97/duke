package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import exceptions.DukeException;
import task.Tasks;
import task.ToDo;
import task.Deadline;
import task.Event;

/**
 * This class deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {

    private static String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Extracts the tasks from duke.txt file when user opens the programme.
     * Returns back the list of tasks.
     */
    public List<Tasks> getTasksFromDatabase() throws DukeException {
        List<Tasks> userToDoListTask = new ArrayList<>();
        List<String> userToDoListString = new ArrayList<>();
        try {
            File file = new File(filePath);
            Scanner s = new Scanner(file);
            while (s.hasNext()) {
                userToDoListString.add(s.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw DukeException.FILE_NOT_FOUND;
        }
        List<String> store = new ArrayList<>();
        for (int i = 0; i < userToDoListString.size(); i += 1) {
            String line = userToDoListString.get(i);
            if (line.trim().isEmpty()) { //ignore empty lines
                continue;
            } else {
                String[] arr = line.split("\\|");
                String type = arr[0].strip();
                String done = arr[1].strip();
                String taskMessage = arr[2].strip();
                Tasks tasks;
                if (type.equals("T")) {
                    tasks = new ToDo(taskMessage, "T");
                } else if (type.equals("D")) {
                    tasks = new Deadline(taskMessage, "D", arr[3].strip());
                } else {
                    tasks = new Event(taskMessage, "E", arr[3].strip());
                }
                if (done.equals("✓")) {
                    tasks.setDone(true);
                } else {
                    tasks.setDone(false);
                }
                userToDoListTask.add(tasks); //convert the line to a task and add to the list
            }
        }

        return userToDoListTask;
    }

    /**
     * Saves tasks into duke.txt file.
     */
    public static void saveTask(List<Tasks> userToDoList) throws DukeException {
        File f = new File(filePath);
        try {
            FileWriter fileWriter = new FileWriter(f);
            for (Tasks task : userToDoList) {
                String taskType = task.getType();
                String line;
                if (taskType == "T") {
                    line = "T | " + task.getStatusIcon() + " | " + task.getDescription();
                } else if (taskType == "D") {
                    line = "D | " + task.getStatusIcon() + " | "
                        + task.getDescription() + " | " + ((Deadline) task).getDeadline();
                } else {
                    line = "E | " + task.getStatusIcon() + " | "
                        + task.getDescription() + " | " + ((Event) task).getTime();
                }
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

}
