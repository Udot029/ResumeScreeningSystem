package com.screener.dsa;

import com.screener.model.Candidate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Candidate Ranking Engine utilizing a PriorityQueue (Max-Heap).
 * 
 * Orders candidate records by match score in descending order.
 * 
 * Algorithmic Complexity:
 * - Heap Building / Insertion: O(N log N) total where N is the number of candidates.
 * - Poll / Extraction: O(N log N) total to extract candidates in strictly sorted order.
 * - Space Complexity: O(N) auxiliary space to maintain heap nodes in memory.
 */
public class CandidateRanker {

    /**
     * Ranks a collection of Candidate objects by match score using a PriorityQueue (Max-Heap).
     * 
     * @param candidates Unsorted list of candidates
     * @return List of candidates sorted in descending order of matchScore
     */
    public List<Candidate> rankCandidates(List<Candidate> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return new ArrayList<>();
        }

        // Custom Comparator configuring PriorityQueue as a Max-Heap based on matchScore.
        // Higher scores receive higher priority. Ties are broken by candidate name alphabetically.
        Comparator<Candidate> maxHeapComparator = new Comparator<Candidate>() {
            @Override
            public int compare(Candidate c1, Candidate c2) {
                int scoreCompare = Double.compare(c2.getMatchScore(), c1.getMatchScore());
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                // Secondary fallback sorting: Alphabetical by candidate name
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        };

        // Initialize PriorityQueue with the max-heap comparator
        PriorityQueue<Candidate> maxHeap = new PriorityQueue<>(candidates.size(), maxHeapComparator);

        // Offer all candidate objects into the Max-Heap O(N log N)
        for (Candidate candidate : candidates) {
            if (candidate != null) {
                maxHeap.offer(candidate);
            }
        }

        // Extract ranked candidates in descending order O(N log N)
        List<Candidate> rankedList = new ArrayList<>(maxHeap.size());
        while (!maxHeap.isEmpty()) {
            rankedList.add(maxHeap.poll());
        }

        return rankedList;
    }

    /**
     * Extracts the top K highest-scoring candidates from a list without fully sorting the rest.
     * Useful for large batch processing to optimize extraction performance.
     * 
     * Time Complexity: O(N log K)
     * 
     * @param candidates List of candidates
     * @param k          Top K count desired
     * @return Top K candidates sorted in descending order
     */
    public List<Candidate> getTopKCandidates(List<Candidate> candidates, int k) {
        if (candidates == null || candidates.isEmpty() || k <= 0) {
            return new ArrayList<>();
        }

        // Min-Heap of fixed capacity K to hold the current top K candidates
        PriorityQueue<Candidate> minHeap = new PriorityQueue<>(k, new Comparator<Candidate>() {
            @Override
            public int compare(Candidate c1, Candidate c2) {
                return Double.compare(c1.getMatchScore(), c2.getMatchScore());
            }
        });

        for (Candidate candidate : candidates) {
            if (candidate == null) continue;

            if (minHeap.size() < k) {
                minHeap.offer(candidate);
            } else if (candidate.getMatchScore() > minHeap.peek().getMatchScore()) {
                minHeap.poll();
                minHeap.offer(candidate);
            }
        }

        // Reconstruct Top K in descending order
        List<Candidate> topKList = new ArrayList<>(minHeap.size());
        while (!minHeap.isEmpty()) {
            topKList.add(0, minHeap.poll()); // Add to front to reverse min-heap extraction
        }

        return topKList;
    }
}
