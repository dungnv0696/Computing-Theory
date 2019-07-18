# Computing-Theory
I. Lyndon Word
Một từ  Lyndon độ dài n > 0 trên bảng chữ  Σ là một từ độ dài  n trên Σ thỏa mãn nó  là từ duy nhất và nhỏ nhất (theo thứ tự từ điển)  trong các từ xoay vòng từ nó.

Tính nhỏ nhất và duy nhất theo phép quay vòng chỉ ra rằng từ Lyndon là khác mọi từ xoay vòng không tầm thường từ nó, và vậy thì nó là từ nguyên thủy.

Ví dụ: Các từ  Lyndon trên bảng chữ nhị phân {0, 1}, được sắp xếp theo độ dài và thứ tự từ điển tạo thành dãy vô hạn với các phần từ đầu tiên là 

0,  1,  01,  001,  011,  0001,  0011,  0111,  00001,  00011,  00101,  00111,  01011,  01111, ...
Nhiệm vụ của bạn: Hãy viết chương trình nhập vào số nguyên n từ người dùng và sinh ra từ  Lyndon  độ dài ≤n trên  {0, 1}.

II. Tính toán với DFA
1. Đoán nhận
Bạn hãy viết chương trình sau.

  Input: bao gồm:

File dfa.jff ở định dạng JFLAP biểu diễn Otomat hữu hạn M, và

một xâu nhị phân w.

  Output: thông báo ra màn hình:

Yes nếu xâu w ∈ L(M).

No nếu xâu w ∉ L(M).

2. Giao và hợp các otomat 
Bạn hãy viết chương trình sau.

Input: Hai file m1.jff và m2.jff ở định dạng JFLAP biểu diễn hai Otomat đơn định M1 và M2.

Output: Hai file giao.jff và hop.jff ở định dạng JFLAP biểu diễn Otomat:

Otomat M giao.ff đoán nhận ngôn ngữ L(M1)∩L(M2), và

Otomat N hop.ff đoán nhận ngôn ngữ L(M1)∪L(M2).

III. Đơn định hóa DFA
  Input:
File nfa.jff ở định dạng JFLAP biểu diễn otomat đa định M.

  Output:
File dfa.jff ở định dạng JFLAP biểu diễn otomat đơn định tương đương với otomat M.

  Yêu cầu:
Ta biết rằng trong Otomat định, những trạng thái không tới được từ trạng thái bắt đầu sẽ không có vai trò trong việc đoán nhận ngôn ngữ, và do đó có thể bỏ đi. Hãy loại bỏ những trạng thái này trong otomat đơn định mà bạn vừa tìm được.

IV. Thao tác với văn phạm
1. Đưa văn phạm về dạng chuẩn Chomsky
Bạn hãy viết chương trình sau.

  Input: 
File G.jff ở định dạng JFLAP biểu diễn văn phạm G.

  Output: 
File Chomsky.jff ở định dạng JFLAP biểu diễn văn phạm G' ở dạng chuẩn Chomsky tương đương với văn phạm G.

2. Thuật toán CYK
Bạn hãy viết chương trình sau.

  Input: 
Gồm hai dữ liệu:

File Chomsky.jff ở định dạng JFLAP biểu diễn văn phạm  (đã ở dạng chuẩn Chomsky); và một xâu s thuộc sigma*.

  Output:
Thông báo ra màn hình cho biết liệu xâu s có sinh bởi văn phạm G hay không.

V. Xây dựng máy Turing EQDFA kiểm tra 2 văn phạm có trùng nhau không?

  Input : Hai otomat đơn định A và B ở dạng file JFLAP.
  
  Output: Cho biết liệu ngôn ngữ sinh bởi DFA A và B có trùng nhau hay không. Nói cách khác, bạn phải thông báo xem liệu L(A)=L(B) hay không. 

VI. Thao tác trên máy Turing

  Input : Máy Turing M ở dạng file JFLAP. Ta giả sử bảng chữ vào {a, b}

  Output: Cho biết các xâu mà M đoán nhận có phải chỉ toàn ký hiệu a không. Nói cách khác, liệu L(M) là tập con của a* hay không?.
