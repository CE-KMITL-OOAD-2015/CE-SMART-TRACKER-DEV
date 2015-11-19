package th.ac.kmitl.ce.ooad.cest.domain;

import java.util.Set;

public class Statistic
{
    private double possibleMax;
    private double mean;
    private double sd;
    private double max;
    private double min;

    public Statistic(Set<ScoreBook> scoreBooks)
    {
        if(scoreBooks.isEmpty())
        {
            this.possibleMax = 0;
            this.mean = 0;
            this.sd = 0;
            this.max = 0;
            this.min = 0;
        }
        else
        {
            double max = 0;
            double min = 0;
            for(ScoreBook scoreBook : scoreBooks)
            {
                this.possibleMax = scoreBook.getMaxSumScore();
                max = min = scoreBook.getSumScore();
                break;
            }

            double sumStudentsScore = 0;
            double sumSquare = 0;
            for(ScoreBook scoreBook : scoreBooks)
            {
                double score = scoreBook.getSumScore();
                sumStudentsScore += score;
                sumSquare += score * score;
                min = (score < min ? score : min);
                max = (score > max ? score : max);
            }
            this.max = max;
            this.min = min;

            int n = scoreBooks.size();
            this.mean = sumStudentsScore / n;
            double variance = (sumSquare - (sumStudentsScore * sumStudentsScore)/n)/(n-1);
            double sd = Math.sqrt(variance);
            this.sd = sd;
        }
    }

    public double getPossibleMax()
    {
        return possibleMax;
    }

    public double getMean()
    {
        return mean;
    }

    public double getSd()
    {
        return sd;
    }

    public double getMax()
    {
        return max;
    }

    public double getMin()
    {
        return min;
    }
}
