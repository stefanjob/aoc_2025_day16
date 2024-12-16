
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class main {
    
    static class State implements Comparable<State> {
        int x, y, direction, score;

        State(int x, int y, int direction, int score) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.score = score;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.score, other.score);
        }
    }

    // Directions: 0 = East, 1 = South, 2 = West, 3 = North
    static final int[] DX = {1, 0, -1, 0};
    static final int[] DY = {0, 1, 0, -1};

    public static int findLowestScore(char[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;

        // Find the start and end positions
        int startX = 0, startY = 0, endX = 0, endY = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'S') {
                    startX = j;
                    startY = i;
                } else if (maze[i][j] == 'E') {
                    endX = j;
                    endY = i;
                }
            }
        }

        // Priority queue for A* search
        PriorityQueue<State> pq = new PriorityQueue<>();
        // Visited array to store the minimum score to reach each state
        int[][][] visited = new int[rows][cols][4];
        for (int[][] layer : visited) {
            for (int[] row : layer) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }

        // Initial state: Start facing East
        pq.offer(new State(startX, startY, 0, 0));
        visited[startY][startX][0] = 0;

        while (!pq.isEmpty()) {
            State current = pq.poll();

            // Check if we've reached the end
            if (current.x == endX && current.y == endY) {
                return current.score;
            }

            // Try moving forward
            int newX = current.x + DX[current.direction];
            int newY = current.y + DY[current.direction];
            if (isInBounds(newX, newY, rows, cols) && maze[newY][newX] != '#') {
                int newScore = current.score + 1;
                if (newScore < visited[newY][newX][current.direction]) {
                    visited[newY][newX][current.direction] = newScore;
                    pq.offer(new State(newX, newY, current.direction, newScore));
                }
            }

            // Try turning clockwise and counterclockwise
            for (int turn = -1; turn <= 1; turn += 2) {
                int newDirection = (current.direction + turn + 4) % 4;
                int newScore = current.score + 1000;
                if (newScore < visited[current.y][current.x][newDirection]) {
                    visited[current.y][current.x][newDirection] = newScore;
                    pq.offer(new State(current.x, current.y, newDirection, newScore));
                }
            }
        }

        return -1; // Should never reach here
    }

    private static boolean isInBounds(int x, int y, int rows, int cols) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    public static void part1()
    {
        System.out.println("AoC Day 16 Part 1");
   
        ArrayList<String> map = new ArrayList<>();
 
        boolean full = true;
        Scanner scanner = null;
 
        try {
            if (full) {
                scanner = new Scanner(new File("input_full.txt"));
            } else {
                scanner = new Scanner(new File("input_test.txt"));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
 
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            map.add(line);
        }
 
        char[][] maze = new char[map.size()][map.get(0).length()];
        int i=0;

        for (String l : map) {
            maze[i] = l.toCharArray();
            i++;
        }
        System.out.println("Lowest score for maze2: " + findLowestScore(maze)); 
    }

    public static void main(String[] args) {
        part1();
    }
}