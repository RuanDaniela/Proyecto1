(defun fibonacci (n)
  (if (<= n 1)
      n
      (+ (fibonacci (- n 1))
         (fibonacci (- n 2)))))

(format t "Resultado: ~a~%" (fibonacci 10))
