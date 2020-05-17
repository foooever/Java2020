## leetCode
### 找零钱问题（20200517）
描述：现存在一堆面值为 1,2,5,11,20,50 面值的硬币，问最少需要多少个硬币才能
找出总值为 N个单位的零钱。

思路1：动态规划转移方程`dp[i] = min {dp[i], dp[i - coins[j]] + 1}`。
```Java
import java.util.Arrays;
class Solution {
    public int coinChange(int[] coins, int amount) {
        
        Arrays.sort(coins);
        
        int[] dp = new int[amount + 1];
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        
        for (int i = 1; i <= amount; i++) {
            for (int j = coins.length - 1; j >= 0; j--) {
                if (i >= coins[j] && dp[i - coins[j]] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);//转移方程
                }
            }
        }
        
        return dp[amount] != Integer.MAX_VALUE ? dp[amount] : -1;
        
    }
}
```
思路2：dp转完全背包问题，表述为每种零钱为一种物品，重量为1，价值为币种面额，每种物品（硬币）无限，
背包容量为找零数额，求装满的最小重量。

完全背包的解决办法为化成0/1背包`dp[i] = min {dp[i], dp[i - c[j]*k] + k}`
```Java
class Solution {
    public int coinChange(int[] coins, int amount) {
        
        int[] dp = new int[amount + 1];
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < coins.length; i++) {
            for (int j = 1; j <= amount; j++) {
                if (j >= coins[i] && dp[j - coins[i]] != Integer.MAX_VALUE) {
                    dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
                } 
            }
        }
        return dp[amount] != Integer.MAX_VALUE ? dp[amount] : -1;
    }
}
```
### 找零钱问题之币种有限（20200517）
思路：转化为多重背包问题，每个币种数量有限即物品数量有限，装满需要的最小重量（个数）。
```Java
class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] num = new int[coins.length]; //每种的数量
        for (int i = 0; i < coins.length; i++) { //此处置为无穷
            num[i] = Integer.MAX_VALUE;
        }
        int[] dp = new int[amount + 1];
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < coins.length; i++) {
            int k = num[i];
            for (int j = 1; j <= amount; j++) {
                if (j >= coins[i] && dp[j - coins[i]] != Integer.MAX_VALUE && k > 0) {
                    if (dp[j] > dp[j - coins[i]] + 1) {
                        dp[j] = dp[j - coins[i]] + 1;
                        k--;
                    }
                } 
            }
        }
        return dp[amount] != Integer.MAX_VALUE ? dp[amount] : -1;
    }
}
```
### 股票买卖之一次买卖（20200517）
```Java
//波峰波谷，最大差值
//.         .(max)
// .       . .
//  .   . .   .
//   . . .
//    .(min)
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int min = 0;
        
        for (int i = 0; i < prices.length; i++){
            if (i == 0) {
                min = prices[i];
            } else {
                maxProfit = Math.max(maxProfit, prices[i] - min);
                min = Math.min(min, prices[i]);
            }
        }
        
        return maxProfit;
    }
}
```
### 股票买卖之二最多两次买卖（20200517）
```Java
//两段式，前面0-i采用上述↑方法，第二段波峰波谷记录
class Solution {
    public int maxProfit(int[] prices) {
        int[] max = new int[prices.length];
        for (int i = prices.length - 1; i >= 0; i--) {
            if (i == prices.length - 1) {
                max[i] = prices[i];
            } else {
                max[i] = Math.max(prices[i], max[i + 1]);
            }
        }
        
        int maxProfit = 0;
        int min = 0;
        int maxEndProfit = 0;
        
        for (int i = 0; i < prices.length; i++) {
            if (i == 0) {
                min = prices[i];
            } else {
                maxProfit = Math.max(maxProfit, prices[i] - min);
                min = Math.min(min, prices[i]);
                int profit2 = 0;
                if (i < prices.length - 2) {
                    int tmp = max[i + 2] - prices[i + 1];
                    profit2 = tmp > 0 ? tmp : 0;
                }
                maxEndProfit = Math.max(maxEndProfit, maxProfit + profit2);
            }
        }
        
        return maxEndProfit;
    }
}
```
### 股票买卖之四最多K次买卖（20200517）
```Java
class Solution {
    public int maxProfit(int k, int[] prices) {
        int len = prices.length;
        if (len < 2 || k < 1) return 0;
        if (k >= len / 2) { //不限次数（股票买卖之三）
            int res = 0;
            for (int i = 1; i < len; i++) {
                if (prices[i] > prices[i - 1]) {
                    res += prices[i] - prices[i - 1]; 
                }
            }
            return res;
        }
        int[][] dp = new int[k][2];
        for (int i = 0; i < k; i++) {
            dp[i][0] = -prices[0];
        }
        for (int i = 0; i < len; i++) {
            dp[0][0] = Math.max(dp[0][0], -prices[i]); //此时必须是买状态（case1：前面买过了；case2：现在买）
            dp[0][1] = Math.max(dp[0][1], dp[0][0] + prices[i]);
            
            for (int j = 1; j < k; j++) {
                //针对第i天，进行买入或卖出的情况进行区分
                dp[j][0] = Math.max(dp[j][0], dp[j - 1][1] - prices[i]); //第i天要么不买（前面买了没卖或者根本不想买），要么前一天卖出了现在买
                dp[j][1] = Math.max(dp[j][1], dp[j][0] + prices[i]);
            }
        }
        return dp[k - 1][1];
        /*
        int[][] t = new int[k + 1][len];
        for (int i = 1; i <= k; i++) {
            int tmpMax =  -prices[0];
            for (int j = 1; j < len; j++) {
                t[i][j] = Math.max(t[i][j - 1], prices[j] + tmpMax); //不卖或者买了在卖
                tmpMax =  Math.max(tmpMax, t[i - 1][j - 1] - prices[j]); //此时第i天，前i-1天卖出此时买入j
            }
        }
        return t[k][len - 1];*/
    }
}
```