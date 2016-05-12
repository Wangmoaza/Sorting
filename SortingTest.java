import java.io.*;
import java.util.*;
import java.lang.Integer;
import java.lang.Math;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		int temp;
		
		for (int i = 0; i < value.length - 1; i++)
		{
			for (int j = 0; j < value.length - i - 1; j++)
			{
				if (value[j] > value[j+1])
				{
					temp = value[j];
					value[j] = value[j+1];
					value[j+1] = temp;
				}
			}
		}

		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		for (int i = 1; i < value.length; i++)
		{
			int item = value[i];
			int loc = i-1;
			
			while (loc >= 0 && value[loc] > item)
			{
				value[loc+1] = value[loc];
				loc--;
			}
			
			value[loc+1] = item;
		}
		
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{
		// copy from lecture slide chapter 12
		for (int i = (value.length - 1)/2; i >= 0; i--)
		{
			percolateDown(value, i, value.length-1);
		}
		
		for (int size = value.length - 2; size >= 0; size--)
		{
			swap(value, 0, size+1);
			percolateDown(value, 0, size);
		}
		
		return (value);
	}
	private static void percolateDown(int[] value, int index, int size)
	{
		// copy from lecture slide chapter 12
		int leftChild = 2 * index;
		int rightChild = 2 * index + 1;
		
		if (leftChild <= size)
		{
			if ((rightChild <= size) && (value[leftChild] < value[rightChild]))
			{
				leftChild = rightChild;
			}
			
			if (value[index] < value[leftChild])
			{
				swap(value, index, leftChild);
				percolateDown(value, leftChild, size);
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value)
	{
		mergeSort(value, 0, value.length-1);
		return (value);
	}
	
	private static void mergeSort(int[] value, int first, int last)
	{
		if (first < last)
		{
			int mid = (first+last)/2;
			mergeSort(value, first, mid);
			mergeSort(value, mid+1, last);
			merge(value, first, mid, last);
		}
	}
	
	private static void merge(int[] value, int first, int mid, int last)
	{
		int[] tempArr = new int[last-first+1];
		int leftIndex = first, rightIndex = mid+1, tempIndex = 0;
		
		while (leftIndex <= mid && rightIndex <= last)
		{
			if (value[leftIndex] < value[rightIndex])
			{
				tempArr[tempIndex] = value[leftIndex];
				leftIndex++;
			}
			
			else
			{
				tempArr[tempIndex] = value[rightIndex];
				rightIndex++;
			}
			
			tempIndex++;
		}
		
		// move rest to tempArr
		if (leftIndex <= mid)
		{
			for (int i = leftIndex; i <= mid; i++)
			{
				tempArr[tempIndex] = value[i];
				tempIndex++;
			}
		}
		
		else if (rightIndex <= last)
		{
			for (int i = rightIndex; i <= last; i++)
			{
				tempArr[tempIndex] = value[i];
				tempIndex++;
			}
		}
		
		for (int k = 0; k < tempArr.length; k++)
			value[first+k] = tempArr[k]; // copy to original array
			
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{
		quickSort(value, 0, value.length-1);
		return (value);
	}

	private static void quickSort(int[] value, int first, int last)
	{
		if (first < last)
		{
			Random rand = new Random();
			int pivotIndex = rand.nextInt(last-first+1) + first;
			
			pivotIndex = partition(value, pivotIndex, first, last);
			quickSort(value, first, pivotIndex-1);
			quickSort(value, pivotIndex+1, last);
		}

	}
	
	private static int partition(int[] value, int pivotIndex, int first, int last)
	{
		// modification from lecture slide chapter 10
		int pivot = value[pivotIndex];
		swap(value, pivotIndex, first);
		int lastSmaller = first, firstUnsorted = first+1;
		
		while(firstUnsorted <= last)
		{
			if (value[firstUnsorted] < pivot)
			{
				swap(value, firstUnsorted, lastSmaller+1);
				lastSmaller++;
				firstUnsorted++;
			}
			else
				firstUnsorted++;
		}
		
		swap(value, lastSmaller, first); // pivotItem in right position
		return lastSmaller; 
	}
	
	private static void swap(int[] value, int index1, int index2)
	{
		int temp = value[index1];
		value[index1] = value[index2];
		value[index2] = temp;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value)
	{
		int absMax = 0;
		int fold;
		
		// find absolute max number
		for (int i = 0; i < value.length; i++)
		{
			if (Math.abs(value[i]) > absMax)
				absMax = value[i];
		}
		
		// make buckets
		ArrayList<LinkedList<Integer>> bucketList = new ArrayList<>();
		
		for (int i = 0; i < 19; i++)
			bucketList.add(new LinkedList<Integer>());
		
		// radix sort
		for (fold = 1; absMax >= fold; fold = fold * 10)
		{
			stableSort(value, bucketList, fold);
		}
		return (value);
	}
	
	private static void stableSort(int[] value, ArrayList<LinkedList<Integer>> bucketList, int fold)
	{
		for (int i = 0; i < value.length; i++)
		{
			int temp = value[i] / fold;			
			bucketList.get(temp % 10 + 9).addLast(value[i]);
		}
		
		int valueIndex = 0;
		for (int bucketIndex = 0; bucketIndex < 19; bucketIndex++)
		{
			while (!bucketList.get(bucketIndex).isEmpty())
			{
				value[valueIndex] = bucketList.get(bucketIndex).removeFirst().intValue();
				valueIndex++;
			}
		}
	}
}