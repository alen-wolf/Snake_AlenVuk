package gameLogic;

import userInput.Direction;
import userInput.KeyInput;
import userInput.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class Play {

    private final int width;
    private final int height;
    private int xAxisFood;
    private int yAxisFood;
    private int snakeLen=3;
    private int points;
    private boolean gameState=true;
    private Direction dir = Direction.RIGHT;

    private final ArrayList<SnakeBody> snake = new ArrayList<>();
    private final KeyInput keyInput = new KeyInput();

    public Play(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void start() throws InterruptedException, IOException {
        snake.add(new SnakeBody(width/2,height/2));
        snake.add(new SnakeBody(width/2,height/2));
        snake.add(new SnakeBody(width/2,height/2));

        xAxisFood = (int) (Math.random()*(width-3)+2);
        yAxisFood = (int) (Math.random()*(height-3)+2);

        while (true){
            if(gameState){
                move(points);
                System.out.println();
                System.out.println(msg("",width/2,Position.Center,"SnakeV"));
                System.out.println();
                create();
                System.out.println(msg("Points: "+points,width+1,Position.Right,"Snake Size: "+snakeLen));
            }
            else {
                System.out.println("Game Over");
                System.out.println(points);
                System.out.println("Press space to continue!");
                if (keyInput.getKey() == Direction.SPACE) {
                    gameState = true;
                    resetGame();
                }
            }
            Thread.sleep(300);
            cls();
        }
    }

    private void resetGame() {
        snake.clear();
        snake.add(new SnakeBody(width / 2, width / 2));
        snake.add(new SnakeBody(width / 2, width / 2));
        snake.add(new SnakeBody(width / 2, width / 2));
        dir = Direction.RIGHT;
        points = 0;
        snakeLen = 3;
    }

    private void move(int score) {
        for (int i = 0; i <=score/100 ; i++) {
            movement();
        }
    }

    public void movement(){
        if(keyInput.getKey()==Direction.UP && dir !=Direction.DOWN)
            dir= Direction.UP;
        if(keyInput.getKey()==Direction.DOWN && dir !=Direction.UP)
            dir=Direction.DOWN;
        if(keyInput.getKey()==Direction.LEFT && dir !=Direction.RIGHT)
            dir=Direction.LEFT;
        if(keyInput.getKey()==Direction.RIGHT && dir !=Direction.LEFT)
            dir=Direction.RIGHT;
        IntStream.iterate(snake.size()-1,i->i>=1,i->i-1).forEachOrdered(i->{
            snake.get(i).x=snake.get(i-1).x;
            snake.get(i).y=snake.get(i-1).y;
        });
        switch (dir){
            case UP -> {
                snake.get(0).y--;
                if(snake.get(0).y==0)
                    snake.get(0).y=height;
            }
            case DOWN -> {
                snake.get(0).y++;
                if(snake.get(0).y==height)
                    snake.get(0).y=0;
            }
            case LEFT -> {
                snake.get(0).x--;
                if(snake.get(0).x==0)
                    snake.get(0).x=width;
            }
            case RIGHT -> {
                snake.get(0).x++;
                if(snake.get(0).x==width)
                    snake.get(0).x=0;
            }
        }
        if(snake.get(0).x==xAxisFood && snake.get(0).y==yAxisFood){
            coolMechanic();
            newFood();
            points=points+2;
        }
        if(headHitsBody()){
            gameState = false;
        }
    }

    private void coolMechanic(){
        Random rand = new Random();
        int chance = rand.nextInt(11-1)+1;

        if(chance%5==0){
            for(int i=0; i<chance; i++) {
                snake.add(new SnakeBody(0, 0));
                snakeLen++;
            }
        }
        else if((chance == 7 || chance == 3) && snakeLen > 8){
            for(int i=0; i<5; i++){
                snake.remove(snake.size()-1);
                snakeLen--;
            }
        }
        else {
            snake.add(new SnakeBody(0,0));
            snakeLen++;
        }
    }

    private boolean headHitsBody() {
        return IntStream.range(1,snake.size()).anyMatch(i->snake.get(0).x==snake.get(i).x && snake.get(0).y==snake.get(i).y );
    }

    private void newFood() {
        xAxisFood=(int)(Math.random()*(width-3)+2);
        yAxisFood=(int)(Math.random()*(height-3)+2);
    }

    private void cls() throws IOException, InterruptedException {
        if(System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        else
            Runtime.getRuntime().exec("clear");
    }

    private String msg(String begin, int lenght, Position pos, String end){
        return switch (pos){
            case Left,Right -> begin+(" ".repeat(lenght-begin.length()-end.length()))+end;
            case Center -> begin+(" ".repeat(lenght-begin.length()-end.length()/2))+end;
        };
    }

    private void create() {
        StringBuilder arena = new StringBuilder();
        for (int i = 0; i<=height; i++){
            for (int j = 0; j<=width; j++){
                if(i==0||i==height||j==0||j==width||(j==xAxisFood && i==yAxisFood)){
                    String food ="*";
                    String barrier="▓";
                    if(j==xAxisFood && i==yAxisFood)
                        arena.append(food);
                    else
                        arena.append(barrier);
                }
                else if(isSnakeBody(i,j)){
                    String body = "O";
                    arena.append(body);
                }
                else
                    arena.append(" ");
            }
            arena.append("\n");
        }
        System.out.println(arena);
    }

    private boolean isSnakeBody(int i, int j) {
        return snake.stream().anyMatch(snakeBody -> j==snakeBody.x && i==snakeBody.y);
    }


}
