document.addEventListener('DOMContentLoaded', function() {
    const csrfTokenElement = document.querySelector('meta[name="_csrf"]');
    const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';

    const resendButton = document.getElementById('resendCodeButton');
    if (resendButton) {
        resendButton.addEventListener('click', function() {
            resendVerificationCode(csrfToken);
        });
    }
});

function resendVerificationCode(csrfToken) {
    fetch('/resendVerificationCode', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        }
    })
        .then(response => {
            if (response.ok) {
                alert('A new verification code has been sent to your email.');
            } else {
                alert('Failed to resend verification code. Please try again.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        });
}
