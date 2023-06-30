document.addEventListener('DOMContentLoaded', () => {
    const dropdownTrigger = document.querySelector('.username-dropdown');
    const dropdownMenu = document.querySelector('.dropdown');

    dropdownTrigger.addEventListener('click', () => {
        dropdownMenu.classList.toggle('active');
    });
});