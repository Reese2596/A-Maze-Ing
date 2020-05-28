package com.example.a_maze_ing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameView extends View {
    private Cell[][] cells;
    private static final int COLS = 7;
    private static final int ROWS = 10;
    private Random rand;
    private float cellSize;
    private float horizontalMargin;
    private float VerticalMargin;
    private Paint wallPaint;
    private static final float WALL_THICKNESS = 4;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        rand = new Random();

        createMaze();
    }

    private void createMaze(){
        cells = new Cell[COLS][ROWS];

        for(int n = 0; n < COLS; n++){
            for(int m = 0; m < ROWS; m++){
                cells[n][m] = new Cell(n, m);
            }
        }

        Stack<Cell> s = new Stack<>();
        Cell next;

        Cell current = cells[0][0];
        current.visited = true;
        do{
            next = getNeighbor(current);
            if(next != null){
                removeWall(current, next);
                s.push(current);
                current = next;
                current.visited = true;
            }
            else{
                current = s.pop();
            }
        }while(!s.empty());
    }

    private void removeWall(Cell curr, Cell next){
        if(curr.col == next.col && curr.row == next.row + 1){
            curr.topWall = false;
            next.bottomWall = false;
        }

        if(curr.col == next.col && curr.row == next.row - 1){
            next.topWall = false;
            curr.bottomWall = false;
        }

        if(curr.col == next.col + 1 && curr.row == next.row){
            curr.leftWall = false;
            next.rightWall = false;
        }

        if(curr.col == next.col - 1 && curr.row == next.row){
            next.leftWall = false;
            curr.rightWall = false;
        }
    }

    private Cell getNeighbor(Cell cell) {
        ArrayList<Cell> neighbor = new ArrayList<>();

        //Base check
        if (cell.col > 0){
            //Check left neighbor
            if (!cells[cell.col - 1][cell.row].visited) {
                neighbor.add(cells[cell.col - 1][cell.row]);
            }
        }
        if (cell.col < COLS - 1){
            //Check right neighbor
            if (!cells[cell.col + 1][cell.row].visited) {
                neighbor.add(cells[cell.col + 1][cell.row]);
            }
        }
        if(cell.row > 0) {
            //Check top neighbor
            if (!cells[cell.col][cell.row - 1].visited) {
                neighbor.add(cells[cell.col][cell.row - 1]);
            }
        }
        if(cell.row > ROWS - 1){
            //Check bottom neighbor
            if (!cells[cell.col][cell.row + 1].visited) {
                neighbor.add(cells[cell.col][cell.row + 1]);
            }
        }
        if(neighbor.size() > 0) {
            int index = rand.nextInt(neighbor.size());
            return neighbor.get(index);
        }
        return null;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.CYAN);

        int width = getWidth();
        int height = getHeight();

        if(width/height < COLS/ROWS){
            cellSize = width/(ROWS+1);
        }
        else{
            cellSize = width/(COLS+1);
        }

        horizontalMargin = (width - COLS * cellSize)/2;
        VerticalMargin = (height - ROWS * cellSize)/2;
        canvas.translate(horizontalMargin, VerticalMargin);

        for(int n = 0; n < COLS; n++){
            for(int m = 0; m < ROWS; m++){
                if(cells[n][m].topWall) {
                    canvas.drawLine(
                            n * cellSize,
                            m * cellSize,
                            (n + 1) * cellSize,
                            m * cellSize,
                            wallPaint
                    );
                }
                if(cells[n][m].leftWall) {
                    canvas.drawLine(
                            n * cellSize,
                            m * cellSize,
                            (n) * cellSize,
                            (m + 1) * cellSize,
                            wallPaint
                    );
                }
                if(cells[n][m].bottomWall) {
                    canvas.drawLine(
                            n * cellSize,
                            (m + 1) * cellSize,
                            (n + 1) * cellSize,
                            (m + 1) * cellSize,
                            wallPaint
                    );
                }
                if(cells[n][m].rightWall){
                    canvas.drawLine(
                            (n + 1) * cellSize,
                            m * cellSize,
                            (n +1) * cellSize,
                            (m + 1) * cellSize,
                            wallPaint
                    );
                }
            }
        }
    }
}
