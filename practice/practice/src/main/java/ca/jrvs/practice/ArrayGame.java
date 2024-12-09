package ca.jrvs.practice;


class ArrayGame {

    public boolean canWin(int[] board, int jumpLength) {
        boolean[] visited = new boolean[board.length];

        return canReachEnd(board, jumpLength, 0, visited);
    }

    private boolean canReachEnd(int[] board, int jumpLength, int position, boolean[] visited) {
        if (position >= board.length) {
            return true;
        }

        if (position < 0 || board[position] == 1 || visited[position]) {
            return false;
        }

        visited[position] = true;


        boolean canWinBackward = canReachEnd(board, jumpLength, position - 1, visited);
        boolean canWinForward = canReachEnd(board, jumpLength, position + 1, visited);
        boolean canWinByJumping = canReachEnd(board, jumpLength, position + jumpLength, visited);

        return canWinBackward || canWinForward || canWinByJumping;
    }

    public static void main(String[] args) {

        ArrayGame game = new ArrayGame();

        int[] board = {0, 0, 0, 1, 1, 1};
        int jumpLength = 5;

        System.out.println(game.canWin(board, jumpLength));
    }
}

