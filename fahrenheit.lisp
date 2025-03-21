(defun factorial (n)
  (if (= n 0)
      1
      (* n (factorial (- n 1)))))

(format t "~a~%" (factorial 5))



