package java.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * Ĭ�ϳ�ʼ������С
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * �����飨���ڿ�ʵ������
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    //����Ĭ�ϴ�С��ʵ���Ĺ��������ʵ����
    //���ǰ�����EMPTY_ELEMENTDATA���������ֳ�������֪������ӵ�һ��Ԫ��ʱ������Ҫ���Ӷ��١�
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * ����ArrayList���ݵ�����
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * ArrayList ��������Ԫ�ظ���
     */
    private int size;

    /**
     * ����ʼ���������Ĺ��캯�����û������ڴ���ArrayList����ʱ�Լ�ָ�����ϵĳ�ʼ��С��
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            //�������Ĳ�������0������initialCapacity��С������
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            //�������Ĳ�������0������������
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            //����������׳��쳣
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    /**
     *Ĭ���޲ι��캯��
     *DEFAULTCAPACITY_EMPTY_ELEMENTDATA Ϊ0.��ʼ��Ϊ10��Ҳ����˵��ʼ��ʵ�ǿ����� ����ӵ�һ��Ԫ�ص�ʱ�����������ű��10
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * ����һ������ָ�����ϵ�Ԫ�ص��б����������ɼ��ϵĵ��������ص�˳��
     */
    public ArrayList(Collection<? extends E> c) {
        //��ָ������ת��Ϊ����
        elementData = c.toArray();
        //���elementData����ĳ��Ȳ�Ϊ0
        if ((size = elementData.length) != 0) {
            // ���elementData����Object�������ݣ�c.toArray���ܷ��صĲ���Object���͵��������Լ����������������жϣ�
            if (elementData.getClass() != Object[].class)
                //��ԭ������Object���͵�elementData��������ݣ���ֵ���µ�Object���͵�elementData����
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // ����������ÿ��������
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * �޸����ArrayListʵ�����������б�ĵ�ǰ��С�� Ӧ�ó������ʹ�ô˲�������С��ArrayListʵ���Ĵ洢��
     */
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }
    //������ArrayList�����ݻ���
    //ArrayList�����ݻ�����������ܣ����ÿ��ֻ����һ����
    //��ôƵ���Ĳ���ᵼ��Ƶ���Ŀ������������ܣ���ArrayList�����ݻ��Ʊ��������������
    /**
     * ���б�Ҫ�����Ӵ�ArrayListʵ������������ȷ��������������Ԫ�ص�����
     * @param   minCapacity   �������С����
     */
    public void ensureCapacity(int minCapacity) {
        //�����true��minExpand��ֵΪ0�������false,minExpand��ֵΪ10
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                // any size if not default element table
                ? 0
                // larger than default for default empty table. It's already
                // supposed to be at default size.
                : DEFAULT_CAPACITY;
        //�����С�����������е��������
        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }
    //1.�õ���С������
    //2.ͨ����С��������
    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            // ��ȡ��Ĭ�ϵ��������͡��������������֮������ֵ
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }
    //�ж��Ƿ���Ҫ����
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            //����grow�����������ݣ����ô˷��������Ѿ���ʼ������
            grow(minCapacity);
    }
    /**
     * Ҫ�������������С
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    /**
     * ArrayList���ݵĺ��ķ�����
     */
    private void grow(int minCapacity) {
        // oldCapacityΪ��������newCapacityΪ������
        int oldCapacity = elementData.length;
        //��oldCapacity ����һλ����Ч���൱��oldCapacity /2��
        //����֪��λ������ٶ�ԶԶ�����������㣬��������ʽ�Ľ�����ǽ�����������Ϊ��������1.5����
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        //Ȼ�����������Ƿ������С��Ҫ������������С����С��Ҫ��������ô�Ͱ���С��Ҫ���������������������
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        //�ټ���������Ƿ񳬳���ArrayList����������������
        //�������ˣ������hugeCapacity()���Ƚ�minCapacity�� MAX_ARRAY_SIZE��
        //���minCapacity����MAX_ARRAY_SIZE������������ΪInterger.MAX_VALUE��������������С��Ϊ MAX_ARRAY_SIZE��
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
    //�Ƚ�minCapacity�� MAX_ARRAY_SIZE
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }
    /**
     *���ش��б��е�Ԫ������
     */
    public int size() {
        return size;
    }

    /**
     * ������б�����Ԫ�أ��򷵻� true ��
     */
    public boolean isEmpty() {
        //ע��=��==������
        return size == 0;
    }
    /**
     * ������б����ָ����Ԫ�أ��򷵻�true ��
     */
    public boolean contains(Object o) {
        //indexOf()���������ش��б���ָ��Ԫ�ص��״γ��ֵ�������������б�������Ԫ�أ���Ϊ-1
        return indexOf(o) >= 0;
    }
    /**
     *���ش��б���ָ��Ԫ�ص��״γ��ֵ�������������б�������Ԫ�أ���Ϊ-1
     */
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                //equals()�����Ƚ�
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * ���ش��б���ָ��Ԫ�ص����һ�γ��ֵ�������������б�����Ԫ�أ��򷵻�-1��.
     */
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }
    /**
     * ���ش�ArrayListʵ����ǳ������ ��Ԫ�ر��������ơ���
     */
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            //Arrays.copyOf������ʵ������ĸ��ƣ����ظ��ƺ�����顣�����Ǳ����Ƶ�����͸��Ƶĳ���
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // �ⲻӦ�÷�������Ϊ�����ǿ��Կ�¡��
            throw new InternalError(e);
        }
    }

    /**
     *����ȷ��˳�򣨴ӵ�һ�������һ��Ԫ�أ�����һ���������б�������Ԫ�ص����顣
     *���ص����齫�ǡ���ȫ�ġ�����Ϊ���б��������������á� �����仰˵����������������һ���µ����飩��
     *��ˣ������߿������ɵ��޸ķ��ص����顣 �˷����䵱�������кͻ��ڼ��ϵ�API֮���������
     */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }
    /**
     * ����ȷ��˳�򷵻�һ���������б�������Ԫ�ص����飨�ӵ�һ�������һ��Ԫ�أ�;
     *���ص����������ʱ������ָ�����������ʱ���͡� ����б��ʺ�ָ�������飬�򷵻����С�
     *���򣬽�Ϊָ�����������ʱ���ͺʹ��б�Ĵ�С����һ�������顣
     *����б�������ָ�������飬����ռ䣨��������б��������ڴ�Ԫ�أ���������ڼ��Ͻ�����������е�Ԫ������Ϊnull ��
     *������ڵ�����֪���б������κο�Ԫ�ص�����²���ȷ���б�ĳ��ȡ���
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // �½�һ������ʱ���͵����飬����ArrayList���������
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        //����System�ṩ��arraycopy()����ʵ������֮��ĸ���
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // Positional Access Operations

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * ���ش��б���ָ��λ�õ�Ԫ�ء�
     */
    public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }
    /**
     * ��ָ����Ԫ���滻���б���ָ��λ�õ�Ԫ�ء�
     */
    public E set(int index, E element) {
        //��index���н��޼��
        rangeCheck(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        //����ԭ�������λ�õ�Ԫ��
        return oldValue;
    }

    /**
     * ��ָ����Ԫ��׷�ӵ����б��ĩβ��
     */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        //���￴��ArrayList���Ԫ�ص�ʵ�ʾ��൱��Ϊ���鸳ֵ
        elementData[size++] = e;
        return true;
    }
    /**
     * �ڴ��б��е�ָ��λ�ò���ָ����Ԫ�ء�
     *�ȵ��� rangeCheckForAdd ��index���н��޼�飻Ȼ����� ensureCapacityInternal ������֤capacity�㹻��
     *�ٽ���index��ʼ֮������г�Ա����һ��λ�ã���element����indexλ�ã����size��1��
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);  // Increments modCount!!
        //arraycopy()���ʵ������֮�临�Ƶķ���һ��Ҫ��һ�£�������õ���arraycopy()����ʵ�������Լ������Լ�
        System.arraycopy(elementData, index, elementData, index + 1,
                size - index);
        elementData[index] = element;
        size++;
    }
    /**
     * ɾ�����б���ָ��λ�õ�Ԫ�ء� ���κκ���Ԫ���ƶ�����ࣨ���������м�ȥһ��Ԫ�أ���
     */
    public E remove(int index) {
        rangeCheck(index);

        modCount++;
        E oldValue = elementData(index);

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work
        //���б���ɾ����Ԫ��
        return oldValue;
    }
    /**
     * ���б���ɾ��ָ��Ԫ�صĵ�һ�����֣�������ڣ��� ����б�������Ԫ�أ�����������ġ�
     *����true��������б����ָ����Ԫ��
     */
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }
    /*
     * Private remove method that skips bounds checking and does not
     * return the value removed.
     */
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }

    /**
     * ���б���ɾ������Ԫ�ء�
     */
    public void clear() {
        modCount++;

        // �����������е�Ԫ�ص�ֵ��Ϊnull
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    /**
     * ��ָ�����ϵ�Iterator���ص�˳��ָ�������е�����Ԫ��׷�ӵ����б��ĩβ��
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * ��ָ�������е�����Ԫ�ز��뵽���б��У���ָ����λ�ÿ�ʼ��
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }
    /**
     * �Ӵ��б���ɾ����������ΪfromIndex ��������toIndex֮���Ԫ�ء�
     *���κκ���Ԫ���ƶ�����ࣨ��������������
     */
    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                numMoved);

        // clear to let GC do its work
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }
    /**
     * �������������Ƿ��ڷ�Χ�ڡ�
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * add��addAllʹ�õ�rangeCheck��һ���汾
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    /**
     * ����IndexOutOfBoundsExceptionϸ����Ϣ
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    /**
     * �Ӵ��б���ɾ��ָ�������а���������Ԫ�ء�
     */
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        //������б��޸��򷵻�true
        return batchRemove(c, false);
    }
    /**
     * ���������б��а�����ָ�������е�Ԫ�ء�
     *���仰˵���Ӵ��б���ɾ�����в�������ָ�������е�����Ԫ�ء�
     */
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }


    /**
     * ���б��е�ָ��λ�ÿ�ʼ�������б��е�Ԫ�أ�����ȷ˳�򣩵��б��������
     *ָ����������ʾ��ʼ���ý����صĵ�һ��Ԫ��Ϊnext �� ��ʼ����previous������ָ��������1��Ԫ�ء�
     *���ص��б��������fail-fast ��
     */
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    /**
     *�����б��е��б�����������ʵ���˳�򣩡�
     *���ص��б��������fail-fast ��
     */
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }
    /**
     *����ȷ��˳�򷵻ظ��б��е�Ԫ�صĵ�������
     *���صĵ�������fail-fast ��
     */
    public Iterator<E> iterator() {
        return new Itr();
    }
}