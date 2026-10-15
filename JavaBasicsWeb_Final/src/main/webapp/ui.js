const messages = {

    "login-invalid":
        ["error", "Invalid username, password or user type."],

    "login-error":
        ["error", "Login could not be completed."],


    "student-success":
        ["success", "Student added successfully."],

    "student-invalid":
        ["error", "Please enter a valid registration number."],

    "student-error":
        ["error", "Student could not be added."],


    "professor-success":
        ["success", "Professor added successfully."],

    "professor-invalid":
        ["error", "Please enter a valid professor number."],

    "professor-error":
        ["error", "Professor could not be added."],


    "course-success":
        ["success", "Course added successfully."],

    "course-invalid":
        ["error", "Semester must be greater than 0."],

    "course-error":
        ["error", "Course could not be added."],


    "enrollment-success":
        ["success", "Student added to course successfully."],

    "enrollment-not-found":
        ["error", "Student or course was not found."],

    "enrollment-invalid":
        ["error", "Please enter a valid registration number."],

    "enrollment-error":
        ["error", "Student could not be added to the course."],


    "grade-success":
        ["success", "Grade saved successfully."],

    "grade-invalid":
        ["error", "Grade must be between 0 and 10."],

    "grade-error":
        ["error", "Grade could not be saved."],


    "assign-success":
        ["success", "Professor assigned successfully."],

    "assign-professor-not-found":
        ["error", "Professor number was not found."],

    "assign-course-not-found":
        ["error", "Course code was not found."],

    "assign-invalid":
        ["error", "Please enter a valid professor number."],

    "assign-error":
        ["error", "Professor could not be assigned."]
};



/* =========================================================
   TOAST NOTIFICATION
========================================================= */

function showToast(message, type) {

    const toast =
        document.createElement("div");


    toast.className =
        "app-toast " +
        (type === "success"
            ? "app-toast-success"
            : "app-toast-error");


    const icon =
        type === "success"
            ? "✓"
            : "!";


    toast.innerHTML =
        "<span class='toast-icon'>" +
        icon +
        "</span>" +

        "<span class='toast-message'></span>" +

        "<button type='button' " +
        "class='toast-close'>×</button>";


    toast.querySelector(
        ".toast-message"
    ).textContent = message;


    toast.querySelector(
        ".toast-close"
    ).addEventListener(
        "click",
        function () {

            toast.remove();
        }
    );


    document.body.appendChild(
        toast
    );


    setTimeout(
        function () {

            toast.classList.add(
                "toast-hide"
            );


            setTimeout(
                function () {

                    toast.remove();

                },
                300
            );

        },
        4000
    );
}



/* =========================================================
   READ ?status=... FROM URL
========================================================= */

const params =
    new URLSearchParams(
        window.location.search
    );


const status =
    params.get("status");


if (status &&
        messages[status]) {

    const type =
        messages[status][0];

    const message =
        messages[status][1];


    showToast(
        message,
        type
    );


    /*
     * Remove the status parameter
     * after showing the notification.
     */

    params.delete("status");


    const remainingParameters =
        params.toString();


    const cleanURL =
        window.location.pathname +
        (remainingParameters
            ? "?" + remainingParameters
            : "");


    window.history.replaceState(
        {},
        "",
        cleanURL
    );
}



/* =========================================================
   CLIENT-SIDE FORM VALIDATION
========================================================= */

const forms =
    document.querySelectorAll(
        "form"
    );


forms.forEach(
    function (form) {

        /*
         * Disable the browser's default
         * validation popup because we use
         * our own toast messages.
         */
        form.noValidate = true;


        form.addEventListener(
            "submit",
            function (event) {

                const invalidField =
                    form.querySelector(
                        ":invalid"
                    );


                if (invalidField) {

                    event.preventDefault();


                    let message =
                        "Please check the entered value.";


                    if (
                        invalidField.validity.valueMissing
                    ) {

                        message =
                            "Please complete all required fields.";
                    }


                    else if (
                        invalidField.validity.rangeUnderflow ||
                        invalidField.validity.rangeOverflow
                    ) {

                        message =
                            invalidField.dataset.rangeMessage ||
                            "Please enter a number within the valid range.";
                    }


                    else if (
                        invalidField.validity.badInput
                    ) {

                        message =
                            "Please enter a valid number.";
                    }


                    else if (
                        invalidField.validity.stepMismatch
                    ) {

                        message =
                            "Please enter a whole number.";
                    }


                    showToast(
                        message,
                        "error"
                    );


                    invalidField.focus();
                }
            }
        );
    }
);



/* =========================================================
   PASSWORD VISIBILITY TOGGLE
========================================================= */

const passwordInputs =
    document.querySelectorAll(
        "input[type='password']"
    );


passwordInputs.forEach(
    function (passwordInput) {

        /*
         * Create a wrapper around
         * the password field.
         */

        const wrapper =
            document.createElement(
                "div"
            );


        wrapper.className =
            "password-wrapper";


        passwordInput.parentNode.insertBefore(
            wrapper,
            passwordInput
        );


        wrapper.appendChild(
            passwordInput
        );


        /*
         * Create the eye button.
         */

        const toggleButton =
            document.createElement(
                "button"
            );


        toggleButton.type =
            "button";


        toggleButton.className =
            "password-toggle";


        toggleButton.textContent =
            "👁";


        toggleButton.setAttribute(
            "aria-label",
            "Show password"
        );


        wrapper.appendChild(
            toggleButton
        );


        /*
         * Toggle between:
         *
         * password → hidden
         * text     → visible
         */

        toggleButton.addEventListener(
            "click",
            function () {

                if (
                    passwordInput.type ===
                    "password"
                ) {

                    passwordInput.type =
                        "text";

                    toggleButton.textContent =
                        "🙈";

                    toggleButton.setAttribute(
                        "aria-label",
                        "Hide password"
                    );

                } else {

                    passwordInput.type =
                        "password";

                    toggleButton.textContent =
                        "👁";

                    toggleButton.setAttribute(
                        "aria-label",
                        "Show password"
                    );
                }


                /*
                 * Return focus to password field.
                 */

                passwordInput.focus();
            }
        );
    }
);