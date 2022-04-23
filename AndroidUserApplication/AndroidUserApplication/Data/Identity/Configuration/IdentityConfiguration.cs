using AndroidUserApplication.Data.Identity.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace AndroidUserApplication.Data.Identity.Configuration
{
    //Налаштування для класів айдентіті
    public class IdentityConfiguration : IEntityTypeConfiguration<AppUserRole>
    {
        public void Configure(EntityTypeBuilder<AppUserRole> builder)
        {
            //Встановлення ідентифікаторів

            builder.HasKey(x => new { x.UserId, x.RoleId });

            //Формування звязків багато до багатьох
            builder.HasOne(virtualElementFromAppUserRole => virtualElementFromAppUserRole.User)
                .WithMany(virtualCollectionFromAppUser => virtualCollectionFromAppUser.UserRoles)
                .HasForeignKey(intElementFromAppUserRole => intElementFromAppUserRole.UserId)
                .IsRequired();

            builder.HasOne(virtualElementFromAppUserRole => virtualElementFromAppUserRole.Role)
                .WithMany(virtualCollectionFromAppRole => virtualCollectionFromAppRole.UserRoles)
                .HasForeignKey(intElementFromAppUserRole => intElementFromAppUserRole.RoleId)
                .IsRequired();
        }
    }
}
